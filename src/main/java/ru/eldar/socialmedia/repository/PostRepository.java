package ru.eldar.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eldar.socialmedia.entity.post.Post;

import java.util.Collection;

public interface PostRepository extends JpaRepository<Post, Long> {
    Collection<Post> findByAuthorId(Long authorId);
}