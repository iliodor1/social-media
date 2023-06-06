package ru.eldar.socialmedia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.eldar.socialmedia.dto.friendship.FriendshipRequestDto;
import ru.eldar.socialmedia.mapper.FriendshipRequestMapper;
import ru.eldar.socialmedia.service.friendship.FriendshipRequestService;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "PostController")
@RequestMapping("/api/auth/friendship")
public class FriendshipRequestController {

    private final FriendshipRequestService friendshipRequestService;
    private final FriendshipRequestMapper mapper;

    @PostMapping("/send/{userId}")
    @Operation(summary = "Request to send friendship request")
    public ResponseEntity<FriendshipRequestDto> sendFriendshipRequest(@PathVariable Long userId, Principal principal) {
        log.info("Request to send friendship request was received");
        var request = friendshipRequestService.sendFriendshipRequest(userId, principal);
        var friendshipRequestDto = mapper.toDto(request);
        log.info("Friendship request sent successfully");

        return ResponseEntity.ok(friendshipRequestDto);
    }

    @PatchMapping("/accept/{userId}")
    @Operation(summary = "Request to accept friendship request")
    public ResponseEntity<FriendshipRequestDto> acceptFriendshipRequest(@PathVariable Long userId, Principal principal) {
        log.info("Request to accept friendship request was received");
        var request = friendshipRequestService.acceptFriendshipRequest(userId, principal);
        var friendshipRequestDto = mapper.toDto(request);
        log.info("Friendship request accepted successfully");

        return ResponseEntity.ok(friendshipRequestDto);
    }

    @PatchMapping("/reject/{userId}")
    @Operation(summary = "Request to reject friendship request")
    public ResponseEntity<FriendshipRequestDto> rejectFriendshipRequest(@PathVariable Long userId, Principal principal) {
        log.info("Request to reject friendship request was received");
        var request = friendshipRequestService.rejectFriendshipRequest(userId, principal);
        var friendshipRequestDto = mapper.toDto(request);
        log.info("Friendship request rejected successfully");

        return ResponseEntity.ok(friendshipRequestDto);
    }

    @PatchMapping("/unsubscribe/{userId}")
    @Operation(summary = "Request to unsubscribe from user")
    public ResponseEntity<FriendshipRequestDto> unsubscribeFromUser(@PathVariable Long userId, Principal principal) {
        log.info("Request to unsubscribe from user was received");
        var request = friendshipRequestService.unsubscribeFromUser(userId, principal);
        var friendshipRequestDto = mapper.toDto(request);
        log.info("User unsubscribed from user {} successfully", userId);

        return ResponseEntity.ok(friendshipRequestDto);
    }
}