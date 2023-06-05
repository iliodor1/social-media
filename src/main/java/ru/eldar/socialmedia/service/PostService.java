package ru.eldar.socialmedia.service;

import ru.eldar.socialmedia.entity.Post;

import java.security.Principal;
import java.util.Collection;

public interface PostService {

    Post create(Post post, Principal principal);

    Post update(Post post, Long postId, Principal principal);

    void delete(Long postId, Principal principal);


    Collection<Post> getByUserId(Long userId);
}
