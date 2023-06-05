package ru.eldar.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    private final String type = "Bearer";
    private String accessToken;
    private String refreshToken;
}
