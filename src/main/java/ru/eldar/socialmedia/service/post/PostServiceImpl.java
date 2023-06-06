package ru.eldar.socialmedia.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.eldar.socialmedia.entity.post.Post;
import ru.eldar.socialmedia.entity.user.User;
import ru.eldar.socialmedia.exeption.ForbiddenException;
import ru.eldar.socialmedia.exeption.NotFoundException;
import ru.eldar.socialmedia.repository.PostRepository;
import ru.eldar.socialmedia.repository.UserRepository;

import java.security.Principal;
import java.util.Collection;

/**
 * PostServiceImpl is an implementation of the PostService interfaces.
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    /**
     * Creates a new post for the authenticated user.
     *
     * @param post      The post to create.
     * @param principal The principal object representing the authenticated user.
     * @return The created post.
     */
    @Override
    public Post create(Post post, Principal principal) {
        var user = getUserByPrincipal(principal);
        post.setAuthor(user);

        return postRepository.save(post);
    }

    /**
     * Updates an existing post for the authenticated user.
     *
     * @param newPost   The updated post data.
     * @param postId    The ID of the post to update.
     * @param principal The principal object representing the authenticated user.
     * @return The updated post.
     */
    @Override
    public Post update(Post newPost, Long postId, Principal principal) {
        var user = getUserByPrincipal(principal);
        var post = getPostById(postId);

        checkPostOwner(user, post);

        post.setId(postId);
        post.setAuthor(user);

        return postRepository.save(post);
    }

    /**
     * Deletes a post with the given ID for the authenticated user.
     *
     * @param postId    The ID of the post to delete.
     * @param principal The principal object representing the authenticated user.
     */
    @Override
    public void delete(Long postId, Principal principal) {
        var user = getUserByPrincipal(principal);
        var post = getPostById(postId);

        checkPostOwner(user, post);

        postRepository.deleteById(postId);
    }

    /**
     * Retrieves all posts created by the user with the specified ID.
     *
     * @param userId The ID of the user whose posts to retrieve.
     * @return A collection of posts created by the user.
     */
    @Override
    public Collection<Post> getByUserId(Long userId) {
        return postRepository.findByAuthorId(userId);
    }


    private User getUserByPrincipal(Principal principal) {
        var username = principal.getName();

        return userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User not found username=%s", username)
                ));
    }

    private Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(String.format("Post not found id=%s", postId)));
    }

    private void checkPostOwner(User user, Post post) {
        if (!(user.getId().equals(post.getAuthor().getId()))) {
            throw new ForbiddenException("Principal user doesn't post creator");
        }
    }
}