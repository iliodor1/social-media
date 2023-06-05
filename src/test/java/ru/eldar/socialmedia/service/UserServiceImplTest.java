package ru.eldar.socialmedia.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.eldar.socialmedia.config.JwtProvider;
import ru.eldar.socialmedia.dto.AuthenticationRequest;
import ru.eldar.socialmedia.entity.User;
import ru.eldar.socialmedia.exeption.EmailNotUniqueException;
import ru.eldar.socialmedia.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
    void whenLoadExistsUserByUsername_thenReturnUserDetails() {
        when(repository.findByEmail(any())).thenReturn(Optional.of(user));

        var loadedUser = userService.loadUserByUsername("test@test.com");

        verify(repository, times(1)).findByEmail(any());
        assertNotNull(loadedUser);
        assertThat(loadedUser.getUsername(), is("user"));
        assertThat(loadedUser.getPassword(), is("password"));
    }

    @Test
    void whenLoadNotExistsUserByUsername_thenThrowUsernameNotFoundException() {
        when(repository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("test@test.com"));
        verify(repository, times(1)).findByEmail("test@test.com");
    }

    @Test
    public void whenCreateUser_thenReturnUserWhenUserIsCreated() {
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
    public void whenUserAlreadyExists_thenThrowEmailNotUniqueException() {
        when(repository.findByEmail("test@test.com")).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(EmailNotUniqueException.class, () -> userService.create(user));
        verify(repository, times(1)).findByEmail("test@test.com");
        verify(repository, times(0)).save(user);
    }

    @Test
    public void whenCredentialsAreCorrect_thenReturnAuthenticationResponse() {
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
        verify(repository, times(1)).save(user);
    }

    @Test
    public void whenCredentialsAreIncorrect_thenThrowBadCredentialsException() {
        when(repository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(encoder.matches("wrong_password", "password")).thenReturn(false);

        assertThrows(BadCredentialsException.class,
                () -> userService.login(new AuthenticationRequest("wrong_email", "password")));
    }

    @Test
    public void refresh_ShouldReturnAuthenticationResponse_WhenRefreshTokenIsValid() {
        String email = "test@test.com";
        String refreshToken = "token";
        String accessToken = "token";
        User user = new User();
        user.setEmail(email);
        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getRefreshClaims(refreshToken)).thenReturn(null);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtProvider.generateAccessToken(user)).thenReturn(accessToken);

        var authenticationResponse = userService.refresh(refreshToken);

        assertThat(authenticationResponse.getAccessToken()).isEqualTo(accessToken);
        assertThat(authenticationResponse.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    public void refresh_ShouldThrowException_WhenRefreshTokenIsInvalid() {
        String refreshToken = "token";
        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(false);

        assertThatThrownBy(() -> userService.refresh(refreshToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Wrong JWT token");
    }

    @Test
    public void refresh_ShouldThrowException_WhenUserNotFound() {
        String email = "test@test.com";
        String refreshToken = "token";
        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getRefreshClaims(refreshToken)).thenReturn(null);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userServiceImpl.refresh(refreshToken))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    public void refresh_ShouldThrowException_WhenRefreshTokenDoesNotMatchStoredToken() {
        String email = "test@test.com";
        String refreshToken = "token";
        String storedRefreshToken = "storedToken";
        User user = new User();
        user.setEmail(email);
        user.setRefreshToken(storedRefreshToken);
        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getRefreshClaims(refreshToken)).thenReturn(null);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> userService.refresh(refreshToken))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Wrong JWT token");
    }

    @Test
    public void refresh_ShouldUpdateStoredRefreshToken() {
        String email = "test@test.com";
        String refreshToken = "token";
        User user = new User();
        user.setEmail(email);
        user.setRefreshToken(refreshToken);
        when(jwtProvider.validateRefreshToken(refreshToken)).thenReturn(true);
        when(jwtProvider.getRefreshClaims(refreshToken)).thenReturn(null);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtProvider.generateAccessToken(user)).thenReturn("token");
        when(jwtProvider.generateRefreshToken(user)).thenReturn("newToken");

        userService.refresh(refreshToken);

        assertThat(user.getRefreshToken()).isEqualTo("newToken");
        assertThat(userService.get(email)).isEqualTo("newToken");
    }
}
}