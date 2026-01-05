package com.fyn.meetup.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meetups")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Meetup {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    private String description;
    private String location;
    private LocalDateTime dateTime;
    private UUID organizerId;
    private int maxAttendees;

    @OneToMany(mappedBy = "meetup", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MeetupAttendee> attendees = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
