package com.fyn.media.repository;

import com.fyn.media.model.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<Media, UUID> {
    List<Media> findByOwnerId(UUID ownerId);
}
