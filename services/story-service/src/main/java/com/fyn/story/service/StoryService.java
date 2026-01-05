package com.fyn.story.service;

import com.fyn.story.model.Story;
import com.fyn.story.repository.StoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoryService {
    private final StoryRepository storyRepository;

    @Transactional
    public Story createStory(Story story) {
        return storyRepository.save(story);
    }

    public List<Story> getActiveStories(UUID userId) {
        return storyRepository.findByUserIdAndIsExpiredFalseOrderByCreatedAtDesc(userId);
    }

    @Scheduled(fixedRate = 60000) // Every minute
    @Transactional
    public void expireStories() {
        LocalDateTime now = LocalDateTime.now();
        List<Story> toExpire = storyRepository.findByIsExpiredFalseAndExpiresAtBefore(now);

        if (!toExpire.isEmpty()) {
            log.info("Expiring {} stories", toExpire.size());
            toExpire.forEach(story -> story.setExpired(true));
            storyRepository.saveAll(toExpire);
        }
    }

    public List<Story> getAllActiveStories() {
        return storyRepository.findByIsExpiredFalseOrderByCreatedAtDesc();
    }
}
