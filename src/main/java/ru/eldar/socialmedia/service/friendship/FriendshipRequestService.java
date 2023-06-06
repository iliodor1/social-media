package ru.eldar.socialmedia.service.friendship;

import ru.eldar.socialmedia.entity.FriendshipRequest;

import java.security.Principal;

/**
 * The interface FriendshipRequest service
 *
 * @author eldar
 */
public interface FriendshipRequestService {

    /**
     * Sends a friendship request from the authenticated user to the specified user.
     *
     * @param userId    the ID of the user to send the friendship request to
     * @param principal the authenticated user's principal object
     * @return the created friendship request
     */
    FriendshipRequest sendFriendshipRequest(Long userId, Principal principal);

    /**
     * Accepts a friendship request with the specified ID.
     *
     * @param id        the ID of the friendship request to accept
     * @param principal the authenticated user's principal object
     * @return the updated friendship request
     */
    FriendshipRequest acceptFriendshipRequest(Long id, Principal principal);

    /**
     * Rejects a friendship request with the specified ID.
     *
     * @param id        the ID of the friendship request to reject
     * @param principal the authenticated user's principal object
     * @return the updated friendship request
     */
    FriendshipRequest rejectFriendshipRequest(Long id, Principal principal);

    /**
     * Unsubscribes from a user by canceling the friendship request with the specified ID.
     *
     * @param id        the ID of the friendship request to cancel
     * @param principal the authenticated user's principal object
     * @return the updated friendship request
     */
    FriendshipRequest unsubscribeFromUser(Long id, Principal principal);
}