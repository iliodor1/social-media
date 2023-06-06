package ru.eldar.socialmedia.service.friendship;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.eldar.socialmedia.entity.FriendshipRequest;
import ru.eldar.socialmedia.entity.User;
import ru.eldar.socialmedia.exeption.BadRequestException;
import ru.eldar.socialmedia.exeption.NotFoundException;
import ru.eldar.socialmedia.repository.FriendshipRequestRepository;
import ru.eldar.socialmedia.repository.UserRepository;

import static ru.eldar.socialmedia.entity.enums.Status.*;

@Service
@RequiredArgsConstructor
public class FriendshipRequestServiceImpl implements FriendshipRequestService {

    private final FriendshipRequestRepository friendshipRepo;
    private final UserRepository userRepository;

    public FriendshipRequest acceptFriendshipRequest(Long id, Authentication authentication) {
        var user = getUserByPrincipal(authentication);

        var request = getFriendshipRequest(id);

        if (user.equals(request.getUser()) && !(request.getStatus().equals(UNSUBSCRIBED))) {
            request.setStatus(ACCEPTED);

            return friendshipRepo.save(request);
        } else {
            throw new BadRequestException("User didn't get friendship request with id=" + id);
        }
    }

    public FriendshipRequest rejectFriendshipRequest(Long id, Authentication authentication) {
        var user = getUserByPrincipal(authentication);

        var request = getFriendshipRequest(id);

        if (user.equals(request.getUser()) && !(request.getStatus().equals(UNSUBSCRIBED))) {
            request.setStatus(REJECTED);

            return friendshipRepo.save(request);
        } else {
            throw new BadRequestException("User didn't get friendship request with id=" + id);
        }
    }

    public FriendshipRequest unsubscribeFromUser(Long id, Authentication authentication) {
        var user = getUserByPrincipal(authentication);

        var request = getFriendshipRequest(id);

        if (user.equals(request.getUser())) {
            request.setStatus(UNSUBSCRIBED);

            return friendshipRepo.save(request);
        } else {
            throw new BadRequestException("User didn't send friendship request with id=" + id);
        }

    }

    public FriendshipRequest sendFriendshipRequest(Long userId, Authentication authentication) {
        var user = getUserById(userId);
        var follower = getUserByPrincipal(authentication);
        FriendshipRequest friendshipRequest;

        var friendshipRequestOptional = friendshipRepo.findByFollowerIdAndUserId(follower.getId(), user.getId());

        if (friendshipRequestOptional.isPresent()) {
            friendshipRequest = friendshipRequestOptional.get();

            if (!(friendshipRequest.getStatus().equals(UNSUBSCRIBED))) {
                throw new BadRequestException("Friendship request already exist");
            }
            friendshipRequest.setStatus(FOLLOWED);
        } else {
            friendshipRequest = FriendshipRequest.builder()
                    .follower(follower)
                    .user(user)
                    .status(FOLLOWED)
                    .build();
        }

        return friendshipRepo.save(friendshipRequest);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found id=" + userId));
    }

    private User getUserByPrincipal(Authentication authentication) {
        var email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(
                        String.format("User not found email=%s", email)
                ));
    }

    private FriendshipRequest getFriendshipRequest(Long id) {
        return friendshipRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Friendship request not found id=" + id));
    }
}

