package ru.eldar.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eldar.socialmedia.entity.Token;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByEmail(String email);
}