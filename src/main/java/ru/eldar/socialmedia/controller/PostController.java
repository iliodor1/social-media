package ru.eldar.socialmedia.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.eldar.socialmedia.dto.PostDto;
import ru.eldar.socialmedia.mapper.PostMapper;
import ru.eldar.socialmedia.service.post.PostService;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/api/auth/posts")
@RequiredArgsConstructor
@Tag(name = "PostController")
public class PostController {

    private final PostMapper postMapper;
    private final PostService postService;

    @PostMapping
    @Operation(summary = "Request to create a new post")
    public PostDto create(@RequestBody @Valid PostDto postDto, Principal principal) {
        var post = postService.create(postMapper.toPost(postDto), principal);

        return postMapper.toDto(post);
    }

    @PutMapping("/{postId}")
    @Operation(summary = "Request to update a post")
    public PostDto update(@RequestBody @Valid PostDto postDto, @PathVariable Long postId, Principal principal) {
        var post = postService.update(postMapper.toPost(postDto), postId, principal);

        return postMapper.toDto(post);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Request to delete a post")
    public void delete(@PathVariable Long postId, Principal principal) {
        postService.delete(postId, principal);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Request to get user's posts by user id")
    public Collection<PostDto> getByUserId(@PathVariable Long userId) {
        var posts = postService.getByUserId(userId);

        return postMapper.toDtos(posts);
    }
}
