package com.fyn.messaging.repository;

import com.fyn.messaging.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    @Query("SELECT c FROM Conversation c JOIN c.participantIds p WHERE p = :participantId ORDER BY c.lastMessageAt DESC")
    List<Conversation> findByParticipantId(UUID participantId);
}
