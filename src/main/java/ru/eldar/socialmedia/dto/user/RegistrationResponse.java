package ru.eldar.socialmedia.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User registration response body")
public class RegistrationResponse {

    @Schema(description = "id", example = "1")
    private String id;

    @Schema(description = "Username", example = "user")
    private String username;

    @Schema(description = "User email", example = "user@email.com")
    private String email;
}
