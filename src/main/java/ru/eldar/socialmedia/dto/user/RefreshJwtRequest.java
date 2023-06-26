package ru.eldar.socialmedia.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Refresh Jwt token request body")
public class RefreshJwtRequest {

    @NotBlank(message = "Refresh token is mandatory")
    @Schema(description = "Refresh Token", example = "refreshToken")
    public String refreshToken;
}
