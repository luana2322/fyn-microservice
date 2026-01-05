package com.fyn.story.repository;

import com.fyn.story.model.Story;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface StoryRepository extends JpaRepository<Story, UUID> {
    List<Story> findByUserIdAndIsExpiredFalseOrderByCreatedAtDesc(UUID userId);

    List<Story> findByIsExpiredFalseAndExpiresAtBefore(LocalDateTime now);
}
