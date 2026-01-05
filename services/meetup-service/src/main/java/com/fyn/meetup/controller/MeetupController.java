package com.fyn.meetup.controller;

import com.fyn.common.dto.response.ApiResponse;
import com.fyn.meetup.model.Meetup;
import com.fyn.meetup.service.MeetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/meetups")
@RequiredArgsConstructor
public class MeetupController {
    private final MeetupService meetupService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> createMeetup(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader,
            @RequestBody Meetup meetup) {
        try {
            System.out.println("Creating meetup: " + meetup.getTitle() + " for user: " + userIdHeader);
            if (userIdHeader != null && !userIdHeader.isEmpty()) {
                meetup.setOrganizerId(UUID.fromString(userIdHeader));
            }
            Meetup saved = meetupService.createMeetup(meetup);
            System.out.println("Meetup saved with ID: " + saved.getId());
            return ResponseEntity.ok(ApiResponse.ok(convertToMap(saved)));
        } catch (Exception e) {
            System.err.println("Error creating meetup: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error("Failed to create meetup: " + e.getMessage()));
        }
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<ApiResponse<Map<String, Object>>> joinMeetup(@PathVariable UUID id,
            @RequestParam UUID userId) {
        Meetup joined = meetupService.joinMeetup(id, userId);
        return ResponseEntity.ok(ApiResponse.ok(convertToMap(joined)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAllMeetups() {
        List<Meetup> meetups = meetupService.getAllMeetups();
        List<Map<String, Object>> content = meetups.stream().map(this::convertToMap).toList();
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("content", content);
        pageData.put("totalElements", meetups.size());
        pageData.put("totalPages", 1);
        pageData.put("number", 0);
        pageData.put("size", meetups.size());
        return ResponseEntity.ok(ApiResponse.ok(pageData));
    }

    @GetMapping("/discover")
    public ResponseEntity<ApiResponse<Map<String, Object>>> discoverMeetups() {
        List<Meetup> meetups = meetupService.discoverMeetups();
        List<Map<String, Object>> content = meetups.stream().map(this::convertToMap).toList();
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("content", content);
        pageData.put("totalElements", meetups.size());
        pageData.put("totalPages", 1);
        pageData.put("number", 0);
        pageData.put("size", meetups.size());
        return ResponseEntity.ok(ApiResponse.ok(pageData));
    }

    @GetMapping("/my-applied")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMyAppliedMeetups(
            @RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        if (userIdHeader == null || userIdHeader.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(null);
        }
        UUID userId = UUID.fromString(userIdHeader);
        List<Meetup> meetups = meetupService.getMyAppliedMeetups(userId);
        List<Map<String, Object>> content = meetups.stream().map(this::convertToMap).toList();
        Map<String, Object> pageData = new HashMap<>();
        pageData.put("content", content);
        pageData.put("totalElements", meetups.size());
        pageData.put("totalPages", 1);
        pageData.put("number", 0);
        pageData.put("size", meetups.size());
        return ResponseEntity.ok(ApiResponse.ok(pageData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getMeetup(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(convertToMap(meetupService.getMeetup(id))));
    }

    private Map<String, Object> convertToMap(Meetup m) {
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("id", m.getId().toString());
        map.put("title", m.getTitle());
        map.put("description", m.getDescription());
        map.put("location", m.getLocation());
        map.put("latitude", m.getLatitude());
        map.put("longitude", m.getLongitude());
        map.put("scheduledAt", m.getScheduledAt() != null ? m.getScheduledAt().toString() : null);
        map.put("expiresAt", m.getExpiresAt() != null ? m.getExpiresAt().toString() : null);
        map.put("durationMinutes", m.getDurationMinutes());
        map.put("category", m.getCategory());
        map.put("meetType", m.getMeetType());
        map.put("maxParticipants", m.getMaxParticipants());
        map.put("acceptedCount", m.getAcceptedCount());
        map.put("pendingMatchCount", m.getPendingMatchCount());
        map.put("status", m.getStatus());
        map.put("confirmationStatus", m.getConfirmationStatus());
        map.put("userHasApplied", false); // Default
        map.put("isPast", false);
        map.put("isExpired", false);
        map.put("createdAt", m.getCreatedAt() != null ? m.getCreatedAt().toString() : null);

        // Add organizer object
        Map<String, Object> organizer = new java.util.HashMap<>();
        organizer.put("id", m.getOrganizerId() != null ? m.getOrganizerId().toString() : "");
        organizer.put("username", "User"); // Default for now
        organizer.put("fullName", "App User");
        organizer.put("avatarUrl", null);
        map.put("organizer", organizer);

        return map;
    }
}
