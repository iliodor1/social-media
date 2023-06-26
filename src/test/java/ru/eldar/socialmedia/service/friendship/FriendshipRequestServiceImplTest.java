package ru.eldar.socialmedia.service.friendship;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.eldar.socialmedia.entity.friendship.FriendshipRequest;
import ru.eldar.socialmedia.entity.user.JwtAuthentication;
import ru.eldar.socialmedia.entity.user.User;
import ru.eldar.socialmedia.exeption.BadRequestException;
import ru.eldar.socialmedia.repository.FriendshipRequestRepository;
import ru.eldar.socialmedia.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.eldar.socialmedia.entity.friendship.enums.Status.*;

@ExtendWith(MockitoExtension.class)
class FriendshipRequestServiceImplTest {
    @Mock
    FriendshipRequestRepository friendshipRequestRepository;
    @Mock
    UserRepository userRepository;
    @InjectMocks
    FriendshipRequestServiceImpl friendshipRequestService;

    private User follower;
    private User user;

    private JwtAuthentication principal;
    private FriendshipRequest friendshipRequest;

    @BeforeEach
    void setUp() {
        follower = User.builder()
                .id(1L)
                .username("follower")
                .email("follower@email.com")
                .build();

        user = User.builder()
                .id(2L)
                .username("username")
                .email("email@email.com")
                .build();

        principal = new JwtAuthentication();

        principal.setAuthenticated(true);
        principal.setUsername("username");

        friendshipRequest = new FriendshipRequest(1L, follower, user, FOLLOWED);
    }

    @Test
    void sendFriendshipRequest_existingRequest_unsubscribedStatus_throwBadRequestException() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.of(user));
        when(friendshipRequestRepository.findByFollowerIdAndUserId(anyLong(), anyLong()))
                .thenReturn(Optional.of(friendshipRequest));

        var exception = assertThrows(BadRequestException.class,
                () -> friendshipRequestService.sendFriendshipRequest(userId, principal));

        assertEquals("Friendship request already exists", exception.getMessage());
    }

    @Test
    void sendFriendshipRequest_existingRequest_acceptedStatus_returnUpdatedRequest() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.of(user));
        when(friendshipRequestRepository.findByFollowerIdAndUserId(anyLong(), anyLong()))
                .thenReturn(Optional.empty());
        when(friendshipRequestRepository.save(any()))
                .thenReturn(friendshipRequest);

        var request = friendshipRequestService.sendFriendshipRequest(userId, principal);

        assertNotNull(request);
        assertEquals(FOLLOWED, request.getStatus());
    }

    @Test
    void acceptFriendshipRequest_existingRequest_validUser_returnUpdatedRequest() {
        Long requestId = 1L;
        var savedRequest = new FriendshipRequest(1L, user, new User(), ACCEPTED);

        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.of(user));
        when(friendshipRequestRepository.findById(requestId)).thenReturn(Optional.of(friendshipRequest));
        when(friendshipRequestRepository.save(any(FriendshipRequest.class))).thenReturn(savedRequest);

        FriendshipRequest result = friendshipRequestService.acceptFriendshipRequest(requestId, principal);

        assertNotNull(result);
        assertEquals(ACCEPTED, result.getStatus());
    }

    @Test
    void rejectFriendshipRequest_existingRequest_validUser_returnUpdatedRequest() {
        Long requestId = 1L;
        var savedRequest = new FriendshipRequest(1L, user, new User(), REJECTED);

        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.of(user));
        when(friendshipRequestRepository.findById(requestId)).thenReturn(Optional.of(friendshipRequest));
        when(friendshipRequestRepository.save(any(FriendshipRequest.class))).thenReturn(savedRequest);

        FriendshipRequest result = friendshipRequestService.acceptFriendshipRequest(requestId, principal);

        assertNotNull(result);
        assertEquals(REJECTED, result.getStatus());
    }


    @Test
    void unsubscribeFromUser_existingRequest_validUser_returnUpdatedRequest() {
        Long requestId = 1L;
        var savedRequest = new FriendshipRequest(1L, user, new User(), UNSUBSCRIBED);

        when(userRepository.findByEmail(principal.getName())).thenReturn(Optional.of(user));
        when(friendshipRequestRepository.findById(requestId)).thenReturn(Optional.of(friendshipRequest));
        when(friendshipRequestRepository.save(any(FriendshipRequest.class))).thenReturn(savedRequest);

        FriendshipRequest result = friendshipRequestService.acceptFriendshipRequest(requestId, principal);

        assertNotNull(result);
        assertEquals(UNSUBSCRIBED, result.getStatus());
    }
}