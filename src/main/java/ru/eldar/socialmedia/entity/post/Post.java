package ru.eldar.socialmedia.entity.post;


import jakarta.persistence.*;
import lombok.*;
import ru.eldar.socialmedia.entity.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "header", nullable = false)
    private String header;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Image> images;

    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }
}
