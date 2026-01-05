package com.fyn.post.controller;

import com.fyn.common.dto.response.ApiResponse;
import com.fyn.post.model.Post;
import com.fyn.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<ApiResponse<Post>> createPost(@RequestBody Post post) {
        return ResponseEntity.ok(ApiResponse.ok(postService.createPost(post)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Post>> getPost(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getPost(id)));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<ApiResponse<List<Post>>> getAuthorPosts(@PathVariable UUID authorId) {
        return ResponseEntity.ok(ApiResponse.ok(postService.getAuthorPosts(authorId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Post>>> getAllPosts() {
        return ResponseEntity.ok(ApiResponse.ok(postService.getAllPosts()));
    }
}
