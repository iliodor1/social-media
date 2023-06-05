package ru.eldar.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link ru.eldar.socialmedia.entity.Image} entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private String name;
    private byte[] data;
}