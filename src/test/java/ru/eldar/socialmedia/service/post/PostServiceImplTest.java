package ru.eldar.socialmedia.service.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.eldar.socialmedia.entity.post.Image;
import ru.eldar.socialmedia.entity.post.Post;
import ru.eldar.socialmedia.entity.user.JwtAuthentication;
import ru.eldar.socialmedia.entity.user.User;
import ru.eldar.socialmedia.repository.PostRepository;
import ru.eldar.socialmedia.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PostRepository postRepository;
    @InjectMocks
    PostServiceImpl postServiceImpl;

    private Post post;

    private User user;

    private JwtAuthentication principal;

    private static final Image IMAGE = new Image(1L, "name", new byte[]{'Z', 'G', 'F', '0', 'Y', 'Q', '=', '='}, null);

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("username")
                .email("email@email.com")
                .build();

        post = Post.builder()
                .id(1L)
                .author(user)
                .text("text")
                .header("header")
                .images(List.of(IMAGE))
                .build();

        principal = new JwtAuthentication();

        principal.setAuthenticated(true);
        principal.setUsername("username");
    }

    @Test
    void whenCreatePost_thenReturnCreatedPost() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.save(any())).thenReturn(post);

        var result = postServiceImpl.create(post, principal);

        assertEquals("text", result.getText());
        assertEquals("header", result.getHeader());
        assertEquals(user, result.getAuthor());
        assertEquals(List.of(IMAGE), result.getImages());
    }

    @Test
    void whenUpdateExistPost_thenReturnUpdatedPost() {
        var updatedPost = Post.builder()
                .id(1L)
                .author(user)
                .text("updatedText")
                .header("updatedHeader")
                .images(List.of(IMAGE))
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.save(any())).thenReturn(updatedPost);
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        var result = postServiceImpl.update(updatedPost, updatedPost.getId(), principal);

        assertEquals("updatedText", result.getText());
        assertEquals("updatedHeader", result.getHeader());
        assertEquals(user, result.getAuthor());
        assertEquals(List.of(IMAGE), result.getImages());
    }

    @Test
    void whenDeleteExistPost_shouldDeletePost() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        postServiceImpl.delete(post.getId(), principal);

        verify(postRepository, times(1)).deleteById(post.getId());
    }

    @Test
    void whenGetPostsByUser_thenReturnListOfPosts() {
        when(postRepository.findByAuthorId(anyLong())).thenReturn(List.of(post));

        var result = postServiceImpl.getByUserId(post.getId());

        assertFalse(result.isEmpty());
    }
}