package ru.eldar.socialmedia.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.eldar.socialmedia.dto.post.PostDto;
import ru.eldar.socialmedia.mapper.PostMapper;
import ru.eldar.socialmedia.service.post.PostService;

import java.security.Principal;
import java.util.Collection;

@Slf4j
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
        log.info("Request to create a new post was received");

        var post = postService.create(postMapper.toPost(postDto), principal);
        log.info("Post created successfully");

        return postMapper.toDto(post);
    }

    @PutMapping("/{postId}")
    @Operation(summary = "Request to update a post")
    public PostDto update(@RequestBody @Valid PostDto postDto, @PathVariable Long postId, Principal principal) {
        log.info("Request to update a post was received");
        var post = postService.update(postMapper.toPost(postDto), postId, principal);
        log.info("Post updated successfully");

        return postMapper.toDto(post);
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "Request to delete a post")
    public void delete(@PathVariable Long postId, Principal principal) {
        log.info("Request to delete a post was received");
        postService.delete(postId, principal);
        log.info("Post deleted successfully");
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Request to get user's posts by user id")
    public Collection<PostDto> getByUserId(@PathVariable Long userId) {
        log.info("Request to get user's posts by user id was received");
        var posts = postService.getByUserId(userId);
        log.info("User's posts got successfully");

        return postMapper.toDtos(posts);
    }
}
