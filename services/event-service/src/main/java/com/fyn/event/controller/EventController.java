package com.fyn.event.controller;

import com.fyn.common.dto.response.ApiResponse;
import com.fyn.event.model.Event;
import com.fyn.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping
    public ResponseEntity<ApiResponse<Event>> createEvent(@RequestBody Event event) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.createEvent(event)));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<ApiResponse<Event>> joinEvent(
            @PathVariable UUID id,
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "GENERAL") String ticketType) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.joinEvent(id, userId, ticketType)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Event>>> getAllEvents() {
        return ResponseEntity.ok(ApiResponse.ok(eventService.getAllEvents()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Event>> getEvent(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(eventService.getEvent(id)));
    }
}
