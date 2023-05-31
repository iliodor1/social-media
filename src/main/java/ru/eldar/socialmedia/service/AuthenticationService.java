package ru.eldar.socialmedia.service;

import ru.eldar.socialmedia.dto.AuthenticationRequest;
import ru.eldar.socialmedia.dto.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse signIn(AuthenticationRequest authenticationRequest);
}
