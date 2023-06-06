package ru.eldar.socialmedia.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Authentication response body")
public class AuthenticationResponse {
    @Schema(description = "Type")
    private final String type = "Bearer";

    @Schema(description = "Access Token", example = "accessToken")
    private String accessToken;

    @Schema(description = "Refresh Token", example = "refreshToken")
    private String refreshToken;
}
