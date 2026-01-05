package com.fyn.messaging.service;

import com.fyn.common.event.message.MessageSentEvent;
import com.fyn.messaging.model.Conversation;
import com.fyn.messaging.model.Message;
import com.fyn.messaging.repository.ConversationRepository;
import com.fyn.messaging.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public Conversation createConversation(List<UUID> participantIds) {
        Conversation conversation = Conversation.builder()
                .participantIds(participantIds)
                .build();
        return conversationRepository.save(conversation);
    }

    @Transactional
    public Message sendMessage(UUID conversationId, UUID senderId, String content) {
        Conversation conversation = conversationRepository.findById(conversationId).orElseThrow();

        // Find recipient (the other participant)
        UUID recipientId = conversation.getParticipantIds().stream()
                .filter(id -> !id.equals(senderId))
                .findFirst()
                .orElse(senderId);

        Message message = Message.builder()
                .conversation(conversation)
                .senderId(senderId)
                .recipientId(recipientId)
                .content(content)
                .build();

        Message savedMessage = messageRepository.save(message);

        conversation.setLastMessageAt(LocalDateTime.now());
        conversationRepository.save(conversation);

        // Publish event to RabbitMQ
        MessageSentEvent event = MessageSentEvent.builder()
                .messageId(savedMessage.getId().toString())
                .conversationId(conversationId.toString())
                .senderId(senderId.toString())
                .recipientId(recipientId.toString())
                .content(content)
                .preview(content.length() > 50 ? content.substring(0, 50) + "..." : content)
                .build();

        rabbitTemplate.convertAndSend("message-exchange", "message.sent", event);

        return savedMessage;
    }

    public List<Conversation> getConversations(UUID userId) {
        return conversationRepository.findByParticipantId(userId);
    }

    public List<Message> getMessages(UUID conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
}
