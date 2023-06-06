package ru.eldar.socialmedia.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Refresh Jwt token response body")
public class RefreshJwtResponse {

    @Schema(description = "Access Token", example = "accessToken")
    private String accessToken;

    @Schema(description = "Refresh Token", example = "refreshToken")
    private String refreshToken;
}
