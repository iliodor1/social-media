package ru.eldar.socialmedia.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link ru.eldar.socialmedia.entity.User}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "UserDto request body")
public class UserDto {

    @Schema(description = "User username", example = "username")
    private String username;

    @Schema(description = "User email", example = "user@email.com")
    private String email;
}