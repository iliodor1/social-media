package ru.eldar.socialmedia.entity.friendship;

import jakarta.persistence.*;
import lombok.*;
import ru.eldar.socialmedia.entity.friendship.enums.Status;
import ru.eldar.socialmedia.entity.user.User;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "friendship_requests",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"follower_id", "user_id"})}
)
public class FriendshipRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

}
