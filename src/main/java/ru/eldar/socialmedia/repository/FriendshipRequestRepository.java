package ru.eldar.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.eldar.socialmedia.entity.FriendshipRequest;
import ru.eldar.socialmedia.entity.User;
import ru.eldar.socialmedia.entity.enums.Status;

import java.util.Optional;

public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, Long> {
    Optional<FriendshipRequest> findByFollowerIdAndUserId(Long followerId, Long userId);


    Optional<FriendshipRequest> findByIdAndUserId(Long id, Long userId);
}