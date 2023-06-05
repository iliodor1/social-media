package ru.eldar.socialmedia.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.eldar.socialmedia.config.JwtProvider;
import ru.eldar.socialmedia.dto.AuthenticationRequest;
import ru.eldar.socialmedia.dto.AuthenticationResponse;
import ru.eldar.socialmedia.entity.Token;
import ru.eldar.socialmedia.entity.User;
import ru.eldar.socialmedia.exeption.EmailNotUniqueException;
import ru.eldar.socialmedia.exeption.NotFoundException;
import ru.eldar.socialmedia.repository.TokenRepository;
import ru.eldar.socialmedia.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email)
                             .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User create(User user) {
        var userOptional = userRepository.findByEmail(user.getEmail());

        if (userOptional.isPresent()) {
            throw new EmailNotUniqueException(
                    String.format("User with email '%s' already exists", user.getEmail())
            );
        }

        return userRepository.save(user);
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        final var user = loadUserByUsername(authenticationRequest.getEmail());

        if (encoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            tokenRepository.save(Token.builder()
                                      .token(refreshToken)
                                      .user((User) user)
                                      .build());
            return new AuthenticationResponse(accessToken, refreshToken);
        } else {
            throw new BadCredentialsException("Wrong password");
        }
    }

    @Override
    public AuthenticationResponse refresh(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final Token token = tokenRepository.findByEmail(email)
                                                           .orElseThrow(()-> new NotFoundException("Token not found"));


            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final var user = loadUserByUsername(email);
                final String accessToken = jwtProvider.generateAccessToken((User) user);
                final String newRefreshToken = jwtProvider.generateRefreshToken((User) user);
                tokenRepository.save(Token.builder()
                                          .token(newRefreshToken)
                                          .build());
                return new AuthenticationResponse(accessToken, newRefreshToken);
            }
        }
        throw new BadCredentialsException("Wrong JWT token");
    }



   /* public AuthenticationResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final var user = userService.loadUserByUsername(login);
                final String accessToken = jwtProvider.generateAccessToken((User) user);
                return new AuthenticationResponse(accessToken, null);
            }
        }
        return new AuthenticationResponse(null, null);
    }*/

    public User getAuthInfo() {
        return (User) SecurityContextHolder.getContext().getAuthentication();
    }

}
