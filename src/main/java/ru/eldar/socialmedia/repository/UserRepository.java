package ru.eldar.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eldar.socialmedia.entity.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}