package com.fyn.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyn.common.dto.response.ApiResponse;
import com.fyn.post.model.Post;
import com.fyn.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final ObjectMapper objectMapper;

    // Endpoint for JSON body
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Post>> createPostJson(@RequestBody Post post) {
        return ResponseEntity.ok(ApiResponse.ok(postService.createPost(post)));
    }

    // Endpoint for multipart/form-data (from Flutter)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Post>> createPostMultipart(
            @RequestPart("payload") MultipartFile payloadFile,
            @RequestPart(value = "media", required = false) List<MultipartFile> mediaFiles) {
        try {
            // Parse payload JSON from multipart file
            String payloadJson = new String(payloadFile.getBytes());
            Map<String, Object> payload = objectMapper.readValue(payloadJson, Map.class);

            Post post = Post.builder()
                    .content((String) payload.get("content"))
                    .authorId(
                            payload.get("authorId") != null ? UUID.fromString((String) payload.get("authorId")) : null)
                    .mediaUrls(new ArrayList<>())
                    .build();

            // TODO: Handle media file uploads to storage and add URLs to post.mediaUrls
            // For now, just create the post without media

            return ResponseEntity.ok(ApiResponse.ok(postService.createPost(post)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Failed to create post: " + e.getMessage()));
        }
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<List<Post>>> getFeed(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID userId = userIdHeader != null ? UUID.fromString(userIdHeader) : null;
        return ResponseEntity.ok(ApiResponse.ok(postService.getFeed(userId, page, size)));
    }

    @GetMapping("/recommended")
    public ResponseEntity<ApiResponse<List<Post>>> getRecommended(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID userId = userIdHeader != null ? UUID.fromString(userIdHeader) : null;
        return ResponseEntity.ok(ApiResponse.ok(postService.getRecommended(userId, page, size)));
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
