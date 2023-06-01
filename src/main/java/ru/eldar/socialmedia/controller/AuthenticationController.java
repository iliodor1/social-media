package ru.eldar.socialmedia.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.eldar.socialmedia.dto.AuthenticationRequest;
import ru.eldar.socialmedia.dto.AuthenticationResponse;
import ru.eldar.socialmedia.dto.RegistrationRequest;
import ru.eldar.socialmedia.entity.Account;
import ru.eldar.socialmedia.mapper.AccountMapper;
import ru.eldar.socialmedia.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final AccountMapper accountMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(service.signIn(authenticationRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<Account> signUp(@RequestBody RegistrationRequest registrationRequest){
        var account = accountMapper.toAccount(registrationRequest);

        return ResponseEntity.ok(service.signUp(account));
    }
}
