package ru.eldar.socialmedia.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.eldar.socialmedia.entity.post.Post;

import java.util.List;

/**
 * A DTO for the {@link Post} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Post dto body")
public class PostDto {
    @Max(value = 5000, message = "Text must not exceed 5000 characters")
    @Schema(description = "Post text", example = "Post text")
    private String text;


    @NotBlank(message = "Header is mandatory")
    @Max(value = 500, message = "Header must not exceed 500 characters")
    @Schema(description = "Post header", example = "Post header")
    private String header;

    @Schema(description = "Post images")
    private List<ImageDto> images;
}