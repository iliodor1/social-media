package ru.eldar.socialmedia.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.eldar.socialmedia.dto.AuthenticationRequest;
import ru.eldar.socialmedia.dto.AuthenticationResponse;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public AuthenticationResponse signIn(AuthenticationRequest authenticationRequest) {
        return null;
    }
}
