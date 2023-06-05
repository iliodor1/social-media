package ru.eldar.socialmedia.service;

import ru.eldar.socialmedia.dto.AuthenticationRequest;
import ru.eldar.socialmedia.dto.AuthenticationResponse;
import ru.eldar.socialmedia.entity.User;

public interface UserService {
    AuthenticationResponse login(AuthenticationRequest authenticationRequest);

    User create(User user);

    AuthenticationResponse refresh(String refreshToken);
}
