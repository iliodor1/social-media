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
import ru.eldar.socialmedia.dto.AuthenticationRequest;
import ru.eldar.socialmedia.dto.AuthenticationResponse;
import ru.eldar.socialmedia.dto.RefreshJwtRequest;
import ru.eldar.socialmedia.dto.user.RegistrationRequest;
import ru.eldar.socialmedia.dto.user.RegistrationResponse;
import ru.eldar.socialmedia.mapper.UserMapper;
import ru.eldar.socialmedia.service.UserService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name="UserController")
public class UserController {

    private final UserService service;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(service.login(authenticationRequest));
    }

    @PostMapping("/register")
    @Operation(summary = "User registration")
    public ResponseEntity<RegistrationResponse> create(@RequestBody @Valid RegistrationRequest registrationRequest){
        log.info("user={} registration request received", registrationRequest.getUsername());

        var user = userMapper.toUser(registrationRequest);
        var registrationResponse = userMapper.toRegistrationResponse(service.create(user));

        log.info("{} registered successfully", registrationResponse);

        return ResponseEntity.ok(registrationResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        final AuthenticationResponse token = service.refresh(request.getRefreshToken());
        return ResponseEntity.ok(token);
    }

}
