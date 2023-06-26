package ru.eldar.socialmedia.service.user;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.eldar.socialmedia.config.JwtProvider;
import ru.eldar.socialmedia.dto.user.AuthenticationRequest;
import ru.eldar.socialmedia.dto.user.AuthenticationResponse;
import ru.eldar.socialmedia.dto.user.RefreshJwtResponse;
import ru.eldar.socialmedia.entity.user.User;
import ru.eldar.socialmedia.exeption.EmailNotUniqueException;
import ru.eldar.socialmedia.exeption.NotFoundException;
import ru.eldar.socialmedia.repository.UserRepository;

/**
 * UserServiceImpl is an implementation of the UserService and UserDetailsService interfaces.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;

    /**
     * Creates a new user.
     *
     * @param user The user to create.
     * @return The created user.
     * @throws EmailNotUniqueException If the user's email is not unique.
     */
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

    /**
     * Authenticates a user and generates access and refresh tokens.
     *
     * @param authenticationRequest The authentication request containing user credentials.
     * @return The authentication response containing access and refresh tokens.
     * @throws BadCredentialsException If the user credentials are invalid.
     */
    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        var email = authenticationRequest.getEmail();

        final var user = userRepository.findByEmail(email).orElseThrow(
                () -> new BadCredentialsException(String.format("User with email '%s' not found", email))
        );

        if (encoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            user.setRefreshToken(refreshToken);

            return new AuthenticationResponse(accessToken, refreshToken);
        } else {
            throw new BadCredentialsException("Wrong password");
        }
    }

    /**
     * Refreshes the access token using a refresh token.
     *
     * @param refreshToken The refresh token.
     * @return The refresh JWT response containing a new access token and refresh token.
     * @throws BadCredentialsException If the refresh token is invalid.
     */
    @Override
    public RefreshJwtResponse refresh(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String email = claims.getSubject();
            var user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new NotFoundException(String.format("User with email '%s' not found", email)));
            var saveRefreshToken = user.getRefreshToken();

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                user.setRefreshToken(newRefreshToken);

                return new RefreshJwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new BadCredentialsException("Wrong JWT token");
    }

    /**
     * Loads a user by username for authentication purposes.
     *
     * @param username The username.
     * @return The UserDetails object representing the user.
     * @throws UsernameNotFoundException If the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(String.format("User with username '%s' not found", username)));
    }
}
