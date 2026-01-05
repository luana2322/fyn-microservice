package com.fyn.event.repository;

import com.fyn.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByHostId(UUID hostId);
}
