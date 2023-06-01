package ru.eldar.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eldar.socialmedia.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);
}