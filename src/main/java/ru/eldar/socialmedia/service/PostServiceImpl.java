package ru.eldar.socialmedia.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.eldar.socialmedia.entity.User;
import ru.eldar.socialmedia.entity.Post;
import ru.eldar.socialmedia.exeption.NotFoundException;
import ru.eldar.socialmedia.repository.UserRepository;
import ru.eldar.socialmedia.repository.PostRepository;

import java.security.Principal;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public Post create(Post post, Principal principal) {
        var user = getUserByPrincipal(principal);
        post.setAuthor(user);

        return postRepository.save(post);
    }

    @Override
    public Post update(Post post, Long postId, Principal principal) {
        var user = getUserByPrincipal(principal);
        post.setId(postId);
        post.setAuthor(user);

        return postRepository.save(post);
    }

    @Override
    public void delete(Long postId, Principal principal) {
        postRepository.findById(postId)
                      .orElseThrow(()->new NotFoundException(String.format("Post not found id=%s", postId)));

        postRepository.deleteById(postId);
    }

    @Override
    public Collection<Post> getByUserId(Long userId) {
        userRepository.findById(userId)
                             .orElseThrow(()->new NotFoundException(
                                     String.format("User not found id=%s", userId)
                             ));

        return postRepository.findByAuthorId(userId);
    }


    private User getUserByPrincipal(Principal principal) {
        var username = principal.getName();

        return userRepository.findByUsername(username)
                             .orElseThrow(()->new NotFoundException(
                                     String.format("User not found username=%s", username)
                             ));
    }
}
