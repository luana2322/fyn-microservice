package com.fyn.user.controller;

import com.fyn.common.dto.response.ApiResponse;
import com.fyn.common.dto.user.UserSummary;
import com.fyn.user.model.UserProfile;
import com.fyn.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

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
