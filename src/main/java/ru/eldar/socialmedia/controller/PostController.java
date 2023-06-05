package ru.eldar.socialmedia.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.eldar.socialmedia.dto.PostDto;
import ru.eldar.socialmedia.entity.Post;
import ru.eldar.socialmedia.mapper.PostMapper;
import ru.eldar.socialmedia.service.PostService;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostMapper postMapper;
    private final PostService postService;

    @PostMapping
    public PostDto create(@RequestBody PostDto postDto, Principal principal) {
        var post = postService.create(postMapper.toPost(postDto), principal);

        return postMapper.toDto(post);
    }

    @PutMapping("/{postId}")
    public PostDto update(@RequestBody PostDto postDto, @PathVariable Long postId, Principal principal) {
        var post = postService.update(postMapper.toPost(postDto), postId, principal);

        return postMapper.toDto(post);
    }

    @DeleteMapping("/{postId}")
    public void delete(@PathVariable Long postId, Principal principal) {
        postService.delete(postId, principal);
    }

    @DeleteMapping("/{userId}")
    public Collection<PostDto> getByUserId(@PathVariable Long userId) {
        var posts =  postService.getByUserId(userId);

        return postMapper.toDtos(posts);
    }
}
