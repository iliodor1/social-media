package ru.eldar.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.eldar.socialmedia.entity.friendship.FriendshipRequest;

import java.util.Optional;

@Repository
public interface FriendshipRequestRepository extends JpaRepository<FriendshipRequest, Long> {
    Optional<FriendshipRequest> findByFollowerIdAndUserId(Long followerId, Long userId);


    Optional<FriendshipRequest> findByIdAndUserId(Long id, Long userId);
}