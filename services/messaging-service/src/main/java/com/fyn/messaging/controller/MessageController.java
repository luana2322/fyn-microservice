package com.fyn.messaging.controller;

import com.fyn.common.dto.response.ApiResponse;
import com.fyn.messaging.model.Conversation;
import com.fyn.messaging.model.Message;
import com.fyn.messaging.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping("/conversations")
    public ResponseEntity<ApiResponse<Conversation>> createConversation(@RequestBody List<UUID> participantIds) {
        return ResponseEntity.ok(ApiResponse.ok(messageService.createConversation(participantIds)));
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<Message>> sendMessage(
            @RequestParam UUID conversationId,
            @RequestParam UUID senderId,
            @RequestBody String content) {
        return ResponseEntity.ok(ApiResponse.ok(messageService.sendMessage(conversationId, senderId, content)));
    }

    @GetMapping("/user/{userId}/conversations")
    public ResponseEntity<ApiResponse<List<Conversation>>> getConversations(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.ok(messageService.getConversations(userId)));
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<ApiResponse<List<Message>>> getMessages(@PathVariable UUID conversationId) {
        return ResponseEntity.ok(ApiResponse.ok(messageService.getMessages(conversationId)));
    }
}
