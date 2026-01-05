package com.fyn.event.service;

import com.fyn.event.model.Event;
import com.fyn.event.model.EventRegistration;
import com.fyn.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    @Transactional
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    @Transactional
    public Event joinEvent(UUID eventId, UUID userId, String ticketType) {
        Event event = eventRepository.findById(eventId).orElseThrow();

        if (event.getRegistrations().size() >= event.getTicketCapacity()) {
            throw new RuntimeException("Event is sold out");
        }

        boolean alreadyRegistered = event.getRegistrations().stream()
                .anyMatch(r -> r.getUserId().equals(userId));

        if (alreadyRegistered) {
            throw new RuntimeException("Already registered for this event");
        }

        EventRegistration registration = EventRegistration.builder()
                .event(event)
                .userId(userId)
                .ticketType(ticketType)
                .build();

        event.getRegistrations().add(registration);
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEvent(UUID id) {
        return eventRepository.findById(id).orElseThrow();
    }
}
