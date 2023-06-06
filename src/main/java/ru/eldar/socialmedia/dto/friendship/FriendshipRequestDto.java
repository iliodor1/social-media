package ru.eldar.socialmedia.dto.friendship;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.eldar.socialmedia.entity.enums.Status;

/**
 * DTO for {@link ru.eldar.socialmedia.entity.FriendshipRequest}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "FriendshipRequestDto request body")
public class FriendshipRequestDto {

    @Schema(description = "Friendship request id", example = "1")
    private Long id;

    @Schema(description = "Follower")
    private UserDto follower;

    @Schema(description = "User")
    private UserDto user;

    @Schema(description = "Status", example = "FOLLOWED")
    private Status status;
}