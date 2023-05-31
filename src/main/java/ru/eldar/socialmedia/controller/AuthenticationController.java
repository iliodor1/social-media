package ru.eldar.socialmedia.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.eldar.socialmedia.dto.AuthenticationResponse;
import ru.eldar.socialmedia.dto.AuthenticationRequest;
import ru.eldar.socialmedia.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/signIn")
    public ResponseEntity<AuthenticationResponse> signIn(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(service.signIn(authenticationRequest));
    }
}
