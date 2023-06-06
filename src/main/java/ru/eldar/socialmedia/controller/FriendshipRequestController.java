package ru.eldar.socialmedia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.eldar.socialmedia.entity.FriendshipRequest;
import ru.eldar.socialmedia.service.friendship.FriendshipRequestService;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "PostController")
@RequestMapping("/api/auth/friendship")
public class FriendshipRequestController {

    private final FriendshipRequestService friendshipRequestService;

    @PostMapping("/send/{userId}")
    @Operation(summary = "Request to send friendship request")
    public ResponseEntity<FriendshipRequest> sendFriendshipRequest(@PathVariable Long userId, Principal principal) {
        log.info("Request to send friendship request was received");
        FriendshipRequest request = friendshipRequestService.sendFriendshipRequest(userId, principal);
        log.info("Friendship request sent successfully");

        return ResponseEntity.ok(request);
    }

    @PatchMapping("/accept/{userId}")
    @Operation(summary = "Request to accept friendship request")
    public ResponseEntity<FriendshipRequest> acceptFriendshipRequest(@PathVariable Long userId, Principal principal) {
        log.info("Request to accept friendship request was received");
        FriendshipRequest request = friendshipRequestService.acceptFriendshipRequest(userId, principal);
        log.info("Friendship request accepted successfully");

        return ResponseEntity.ok(request);
    }

    @PatchMapping("/reject/{userId}")
    @Operation(summary = "Request to reject friendship request")
    public ResponseEntity<FriendshipRequest> rejectFriendshipRequest(@PathVariable Long userId, Principal principal) {
        log.info("Request to reject friendship request was received");
        FriendshipRequest request = friendshipRequestService.rejectFriendshipRequest(userId, principal);
        log.info("Friendship request rejected successfully");

        return ResponseEntity.ok(request);
    }

    @PatchMapping("/unsubscribe/{userId}")
    @Operation(summary = "Request to unsubscribe from user")
    public ResponseEntity<FriendshipRequest> unsubscribeFromUser(@PathVariable Long userId, Principal principal) {
        log.info("Request to unsubscribe from user was received");
        FriendshipRequest request = friendshipRequestService.unsubscribeFromUser(userId, principal);
        log.info("User unsubscribed from user {} successfully", userId);

        return ResponseEntity.ok(request);
    }
}