package com.fyn.user.controller;

import com.fyn.common.dto.response.ApiResponse;
import com.fyn.common.dto.user.UserSummary;
import com.fyn.user.dto.UserDto;
import com.fyn.user.model.UserProfile;
import com.fyn.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(
            @RequestHeader(value = "X-User-Username", required = false) String username) {
        if (username == null || username.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Missing X-User-Username header"));
        }
        return ResponseEntity.ok(ApiResponse.ok(userService.getUserByUsername(username)));
    }

    @GetMapping("/following/ids")
    public ResponseEntity<ApiResponse<List<String>>> getFollowingIds(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        // TODO: Implement follow logic - for now return empty list
        return ResponseEntity.ok(ApiResponse.ok(Collections.emptyList()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserSummary>>> searchUsers(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(userService.searchUsers(query, page, size)));
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<ApiResponse<UserProfile>> getProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getProfile(userId)));
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<ApiResponse<UserProfile>> updateProfile(
            @PathVariable UUID userId, @RequestBody UserProfile profile) {
        return ResponseEntity.ok(ApiResponse.ok(userService.updateProfile(userId, profile)));
    }

    @GetMapping("/{userId}/summary")
    public ResponseEntity<ApiResponse<UserSummary>> getUserSummary(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getUserSummary(userId)));
    }
}
