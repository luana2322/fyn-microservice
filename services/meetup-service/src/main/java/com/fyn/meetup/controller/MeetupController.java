package com.fyn.meetup.controller;

import com.fyn.common.dto.response.ApiResponse;
import com.fyn.meetup.model.Meetup;
import com.fyn.meetup.service.MeetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/meetups")
@RequiredArgsConstructor
public class MeetupController {
    private final MeetupService meetupService;

    @PostMapping
    public ResponseEntity<ApiResponse<Meetup>> createMeetup(@RequestBody Meetup meetup) {
        return ResponseEntity.ok(ApiResponse.ok(meetupService.createMeetup(meetup)));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<ApiResponse<Meetup>> joinMeetup(@PathVariable UUID id, @RequestParam UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok(meetupService.joinMeetup(id, userId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Meetup>>> getAllMeetups() {
        return ResponseEntity.ok(ApiResponse.ok(meetupService.getAllMeetups()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Meetup>> getMeetup(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(meetupService.getMeetup(id)));
    }
}
