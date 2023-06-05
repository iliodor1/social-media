package ru.eldar.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO for the {@link ru.eldar.socialmedia.entity.Post} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private String text;
    private String header;
    private List<ImageDto> images;
}