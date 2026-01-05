package com.fyn.story.controller;

import com.fyn.common.dto.response.ApiResponse;
import com.fyn.story.model.Story;
import com.fyn.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stories")
@RequiredArgsConstructor
public class StoryController {
    private final StoryService storyService;

    @PostMapping
    public ResponseEntity<ApiResponse<Story>> createStory(@RequestBody Story story) {
        return ResponseEntity.ok(ApiResponse.ok(storyService.createStory(story)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Story>>> getAllActiveStories() {
        return ResponseEntity.ok(ApiResponse.ok(storyService.getAllActiveStories()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Story>>> getActiveStories(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok(storyService.getActiveStories(userId)));
    }
}
