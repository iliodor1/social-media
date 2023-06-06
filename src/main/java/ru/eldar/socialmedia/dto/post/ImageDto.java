package ru.eldar.socialmedia.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.eldar.socialmedia.entity.post.Image;

/**
 * A DTO for the {@link Image} entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Image dto body")
public class ImageDto {

    @NotBlank(message = "Name is mandatory")
    @Schema(description = "Image name", example = "name")
    private String name;

    @NotEmpty(message = "Data must not be empty")
    @Schema(description = "Image data", example = "ZGF0YQ==")
    private byte[] data;
}