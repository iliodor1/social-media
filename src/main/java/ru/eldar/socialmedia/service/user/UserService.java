package ru.eldar.socialmedia.service.user;

import ru.eldar.socialmedia.dto.user.AuthenticationRequest;
import ru.eldar.socialmedia.dto.user.AuthenticationResponse;
import ru.eldar.socialmedia.dto.user.RefreshJwtResponse;
import ru.eldar.socialmedia.entity.User;

/**
 * The interface User service
 *
 * @author eldar
 */
public interface UserService {
    /**
     * Login authentication method
     *
     * @param authenticationRequest the authentication request
     * @return the authentication response
     */
    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    /**
     * Create user method.
     *
     * @param user the user
     * @return the user
     */
    User create(User user);

    /**
     * Refresh new jwt token method
     *
     * @param refreshToken old refresh token
     * @return new refresh jwt token
     */
    RefreshJwtResponse refresh(String refreshToken);
}
