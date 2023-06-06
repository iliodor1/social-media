package ru.eldar.socialmedia.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.eldar.socialmedia.config.JwtProvider;
import ru.eldar.socialmedia.dto.user.AuthenticationRequest;
import ru.eldar.socialmedia.entity.User;
import ru.eldar.socialmedia.exeption.EmailNotUniqueException;
import ru.eldar.socialmedia.exeption.NotFoundException;
import ru.eldar.socialmedia.repository.UserRepository;
import ru.eldar.socialmedia.service.user.UserServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    UserRepository repository;

    @Mock
    PasswordEncoder encoder;
    @Mock
    JwtProvider jwtProvider;


    @InjectMocks
    UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setup() {
        user = User.builder()
                .id(1L)
                .username("user")
                .email("test@test.com")
                .password("password")
                .refreshToken("refreshToken")
                .build();
    }

    @Test
    void whenCreateUser_thenReturnUserWhenUserIsCreated() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(User.class))).thenReturn(user);

        var createdUser = userService.create(user);

        assertNotNull(createdUser);
        assertEquals("test@test.com", createdUser.getEmail());
        assertEquals("password", createdUser.getPassword());
        verify(repository, times(1)).findByEmail("test@test.com");
        verify(repository, times(1)).save(user);
    }

    @Test
    void whenUserAlreadyExists_thenThrowEmailNotUniqueException() {
        when(repository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        assertThrows(EmailNotUniqueException.class, () -> userService.create(user));
        verify(repository, times(1)).findByEmail("test@test.com");
        verify(repository, times(0)).save(user);
    }

    @Test
    void whenCredentialsAreCorrect_thenReturnAuthenticationResponse() {
        when(repository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(encoder.matches("password", "password")).thenReturn(true);
        when(jwtProvider.generateAccessToken(user)).thenReturn("access_token");
        when(jwtProvider.generateRefreshToken(user)).thenReturn("refresh_token");

        var response = userService.login(new AuthenticationRequest("test@test.com", "password"));

        assertNotNull(response);
        assertEquals("access_token", response.getAccessToken());
        assertEquals("refresh_token", response.getRefreshToken());
        verify(repository, times(1)).findByEmail("test@test.com");
        verify(encoder, times(1)).matches("password", "password");
        verify(jwtProvider, times(1)).generateAccessToken(user);
        verify(jwtProvider, times(1)).generateRefreshToken(user);
    }

    @Test
    void whenCredentialsAreIncorrect_thenThrowBadCredentialsException() {
        var authenticationRequest = new AuthenticationRequest("test@test.com", "wrong_password");

        when(repository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(encoder.matches("wrong_password", "password")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userService.login(authenticationRequest));
    }

    @Test
    void refresh_ShouldReturnAuthenticationResponse_WhenRefreshTokenIsValid() {
        String email = "test@test.com";
        String refreshToken = "token";
        String newRefreshToken = "newRefreshToken";
        String accessToken = "token";

        Claims claims = new DefaultClaims();
        claims.setSubject(email);
        user.setRefreshToken(refreshToken);

        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getRefreshClaims(refreshToken)).thenReturn(claims);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtProvider.generateAccessToken(user)).thenReturn(accessToken);
        when(jwtProvider.generateRefreshToken(user)).thenReturn(newRefreshToken);

        var refreshJwtResponse = userService.refresh(refreshToken);

        assertEquals(accessToken, refreshJwtResponse.getAccessToken());
        assertEquals(newRefreshToken, refreshJwtResponse.getRefreshToken());
    }

    @Test
    void refresh_ShouldThrowException_WhenRefreshTokenIsInvalid() {
        String refreshToken = "token";
        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(false);

        assertThatThrownBy(() -> userService.refresh(refreshToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Wrong JWT token");
    }

    @Test
    void refresh_ShouldThrowException_WhenUserNotFound() {
        String email = "test@test.com";
        String refreshToken = "token";
        Claims claims = new DefaultClaims();
        claims.setSubject(email);

        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getRefreshClaims(refreshToken)).thenReturn(claims);
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.refresh(refreshToken))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with email 'test@test.com' not found");
    }

    @Test
    void refresh_ShouldThrowException_WhenRefreshTokenDoesNotMatchStoredToken() {
        String email = "test@test.com";
        String refreshToken = "token";
        String storedRefreshToken = "storedToken";
        User user = new User();
        user.setEmail(email);
        user.setRefreshToken(storedRefreshToken);
        Claims claims = new DefaultClaims();
        claims.setSubject(email);

        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getRefreshClaims(refreshToken)).thenReturn(claims);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.refresh(refreshToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Wrong JWT token");
    }

}