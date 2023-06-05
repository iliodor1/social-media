package ru.eldar.socialmedia.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User registration data")
public class RegistrationRequest {

    @NotBlank(message = "Username is mandatory")
    @Schema(description = "Username", example = "user")
    private String username;

    @Email(message = "Invalid email")
    @Schema(description = "User email", example = "user@email.com")
    private String email;

    @NotBlank(message = "Password must be not blank")
    @Size(min = 6, max = 30, message = "Password must contain from 6 to 30 characters")
    @Schema(description = "User password", example = "password")
    private String password;

}
