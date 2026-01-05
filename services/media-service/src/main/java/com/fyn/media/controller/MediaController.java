package com.fyn.media.controller;

import com.fyn.common.dto.response.ApiResponse;
import com.fyn.media.model.Media;
import com.fyn.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<Media>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("ownerId") UUID ownerId) {
        return ResponseEntity.ok(ApiResponse.ok(mediaService.uploadFile(file, ownerId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Media>> getMedia(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(mediaService.getMedia(id)));
    }
}
