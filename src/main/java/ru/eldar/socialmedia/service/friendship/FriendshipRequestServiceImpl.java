package ru.eldar.socialmedia.service.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.eldar.socialmedia.entity.friendship.FriendshipRequest;
import ru.eldar.socialmedia.entity.user.User;
import ru.eldar.socialmedia.entity.friendship.enums.Status;
import ru.eldar.socialmedia.exeption.BadRequestException;
import ru.eldar.socialmedia.exeption.NotFoundException;
import ru.eldar.socialmedia.repository.FriendshipRequestRepository;
import ru.eldar.socialmedia.repository.UserRepository;

import java.security.Principal;

import static ru.eldar.socialmedia.entity.friendship.enums.Status.*;

/**
 * FriendshipRequestServiceImpl is an implementation of the FriendshipRequestService interfaces.
 */
@Service
@RequiredArgsConstructor
public class FriendshipRequestServiceImpl implements FriendshipRequestService {

    private final FriendshipRequestRepository friendshipRepo;
    private final UserRepository userRepository;

    /**
     * Sends a friendship request from the authenticated user to the specified user.
     *
     * @param userId    the ID of the user to send the friendship request to
     * @param principal the authenticated user's principal object
     * @return the created friendship request
     * @throws BadRequestException if a friendship request already exists between the users
     */
    public FriendshipRequest sendFriendshipRequest(Long userId, Principal principal) {
        var user = getUserById(userId);
        var follower = getUserByPrincipal(principal);
        FriendshipRequest friendshipRequest;

        var friendshipRequestOptional = friendshipRepo.findByFollowerIdAndUserId(follower.getId(), user.getId());

        if (friendshipRequestOptional.isPresent()) {
            friendshipRequest = friendshipRequestOptional.get();

            if (!(friendshipRequest.getStatus().equals(UNSUBSCRIBED))) {
                throw new BadRequestException("Friendship request already exists");
            }
            friendshipRequest.setStatus(FOLLOWED);
        } else {
            friendshipRequest = FriendshipRequest.builder()
                    .follower(follower)
                    .user(user)
                    .status(FOLLOWED)
                    .build();
        }

        return friendshipRepo.save(friendshipRequest);
    }

    /**
     * Accepts a friendship request with the specified ID.
     *
     * @param id        the ID of the friendship request to accept
     * @param principal the authenticated user's principal object
     * @return the updated friendship request
     * @throws BadRequestException if the friendship request doesn't exist or the user didn't receive the request
     */
    public FriendshipRequest acceptFriendshipRequest(Long id, Principal principal) {
        return updateFriendshipRequestStatus(id, principal, ACCEPTED);
    }

    /**
     * Rejects a friendship request with the specified ID.
     *
     * @param id        the ID of the friendship request to reject
     * @param principal the authenticated user's principal object
     * @return the updated friendship request
     * @throws BadRequestException if the friendship request doesn't exist or the user didn't receive the request
     */
    public FriendshipRequest rejectFriendshipRequest(Long id, Principal principal) {
        return updateFriendshipRequestStatus(id, principal, REJECTED);
    }

    /**
     * Unsubscribes from a user by canceling the friendship request with the specified ID.
     *
     * @param id        the ID of the friendship request to cancel
     * @param principal the authenticated user's principal object
     * @return the updated friendship request
     * @throws BadRequestException if the friendship request doesn't exist or the user didn't send the request
     */
    public FriendshipRequest unsubscribeFromUser(Long id, Principal principal) {
        return updateFriendshipRequestStatus(id, principal, UNSUBSCRIBED);
    }

    private FriendshipRequest updateFriendshipRequestStatus(Long id, Principal principal, Status status) {
        var user = getUserByPrincipal(principal);
        var request = getFriendshipRequest(id);

        if (user.equals(request.getUser()) && !(request.getStatus().equals(UNSUBSCRIBED))) {
            request.setStatus(status);
            return friendshipRepo.save(request);
        } else {
            throw new BadRequestException("User didn't get friendship request with id=" + id);
        }
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found id=" + userId));
    }

    private User getUserByPrincipal(Principal principal) {
        var email = principal.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User not found email=%s", email)
                ));
    }

    private FriendshipRequest getFriendshipRequest(Long id) {
        return friendshipRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Friendship request not found id=" + id));
    }
}
