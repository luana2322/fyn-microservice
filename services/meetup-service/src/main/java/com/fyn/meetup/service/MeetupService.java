package com.fyn.meetup.service;

import com.fyn.meetup.model.Meetup;
import com.fyn.meetup.model.MeetupAttendee;
import com.fyn.meetup.repository.MeetupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeetupService {
    private final MeetupRepository meetupRepository;

    @Transactional
    public Meetup createMeetup(Meetup meetup) {
        return meetupRepository.save(meetup);
    }

    @Transactional
    public Meetup joinMeetup(UUID meetupId, UUID userId) {
        Meetup meetup = meetupRepository.findById(meetupId).orElseThrow();

        if (meetup.getAttendees().size() >= meetup.getMaxParticipants()) {
            throw new RuntimeException("Meetup is full");
        }

        boolean alreadyJoined = meetup.getAttendees().stream()
                .anyMatch(a -> a.getUserId().equals(userId));

        if (alreadyJoined) {
            throw new RuntimeException("Already joined");
        }

        MeetupAttendee attendee = MeetupAttendee.builder()
                .meetup(meetup)
                .userId(userId)
                .build();

        meetup.getAttendees().add(attendee);
        return meetupRepository.save(meetup);
    }

    public List<Meetup> getAllMeetups() {
        return meetupRepository.findAll();
    }

    public Meetup getMeetup(UUID id) {
        return meetupRepository.findById(id).orElseThrow();
    }

    public List<Meetup> discoverMeetups() {
        // Return all active meetups for discovery
        return meetupRepository.findAll();
    }

    public List<Meetup> getMyAppliedMeetups(UUID userId) {
        // Return meetups where user is an attendee
        return meetupRepository.findByAttendeesUserId(userId);
    }
}
