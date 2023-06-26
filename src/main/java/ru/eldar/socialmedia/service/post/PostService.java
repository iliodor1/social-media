package ru.eldar.socialmedia.service.post;

import ru.eldar.socialmedia.entity.post.Post;

import java.security.Principal;
import java.util.Collection;

/**
 * The interface Post service
 *
 * @author eldar
 */
public interface PostService {

    /**
     * Creates a new post for the authenticated user.
     *
     * @param post      The post to create.
     * @param principal The principal object representing the authenticated user.
     * @return The created post.
     */
    Post create(Post post, Principal principal);

    /**
     * Updates an existing post for the authenticated user.
     *
     * @param post      The updated post data.
     * @param postId    The ID of the post to update.
     * @param principal The principal object representing the authenticated user.
     * @return The updated post.
     */
    Post update(Post post, Long postId, Principal principal);

    /**
     * Deletes a post with the given ID for the authenticated user.
     *
     * @param postId    The ID of the post to delete.
     * @param principal The principal object representing the authenticated user.
     */
    void delete(Long postId, Principal principal);

    /**
     * Retrieves all posts created by the user with the specified ID.
     *
     * @param userId The ID of the user whose posts to retrieve.
     * @return A collection of posts created by the user.
     */
    Collection<Post> getByUserId(Long userId);
}