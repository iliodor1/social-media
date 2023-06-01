package ru.eldar.socialmedia.service;

import ru.eldar.socialmedia.dto.AuthenticationRequest;
import ru.eldar.socialmedia.dto.AuthenticationResponse;
import ru.eldar.socialmedia.entity.Account;

public interface AuthenticationService {
    AuthenticationResponse signIn(AuthenticationRequest authenticationRequest);

    Account signUp(Account account);
}
