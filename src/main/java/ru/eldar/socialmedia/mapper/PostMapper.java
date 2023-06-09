package ru.eldar.socialmedia.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.eldar.socialmedia.dto.post.PostDto;
import ru.eldar.socialmedia.entity.post.Post;

import java.util.Collection;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final ModelMapper mapper;

    public Post toPost(PostDto postDto) {
        return mapper.map(postDto, Post.class);
    }

    public PostDto toDto(Post post) {
        return mapper.map(post, PostDto.class);
    }

    public Collection<PostDto> toDtos(Collection<Post> posts) {
        return posts.stream().map(this::toDto).toList();
    }
}
