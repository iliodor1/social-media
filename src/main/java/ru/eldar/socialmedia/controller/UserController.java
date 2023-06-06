package ru.eldar.socialmedia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.eldar.socialmedia.dto.user.*;
import ru.eldar.socialmedia.mapper.UserMapper;
import ru.eldar.socialmedia.service.user.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "UserController")
public class UserController {

    private final UserService service;
    private final UserMapper userMapper;

    @PostMapping("/login")
    @Operation(summary = "User login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        log.info("User authentication request received");
        var authenticationResponse = service.login(authenticationRequest);
        log.info("User authentication successfully");

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/register")
    @Operation(summary = "User registration")
    public ResponseEntity<RegistrationResponse> create(@RequestBody @Valid RegistrationRequest registrationRequest) {
        log.info("User={} registration request received", registrationRequest.getUsername());

        var user = userMapper.toUser(registrationRequest);
        var registrationResponse = userMapper.toRegistrationResponse(service.create(user));

        log.info("{} registered successfully", registrationResponse.getUsername());

        return ResponseEntity.ok(registrationResponse);
    }

    @Operation(summary = "User gets new refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<RefreshJwtResponse> getNewRefreshToken(@RequestBody @Valid RefreshJwtRequest request) {
        log.info("User request for a new refresh token received");
        var token = service.refresh(request.getRefreshToken());
        log.info("User got the new refresh token successfully");

        return ResponseEntity.ok(token);
    }

}
