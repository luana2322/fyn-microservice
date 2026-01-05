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
    private Double latitude;
    private Double longitude;
    private LocalDateTime scheduledAt;
    private LocalDateTime expiresAt;
    private Integer durationMinutes;
    private String category;
    private String meetType; // ONE_TO_ONE, GROUP
    private UUID organizerId;
    private Integer maxParticipants;

    @Builder.Default
    private Integer acceptedCount = 0;
    @Builder.Default
    private Integer pendingMatchCount = 0;
    @Builder.Default
    private String status = "OPEN";
    @Builder.Default
    private String confirmationStatus = "PENDING";

    @OneToMany(mappedBy = "meetup", cascade = CascadeType.ALL)
    @Builder.Default
    private List<MeetupAttendee> attendees = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null)
            status = "OPEN";
        if (confirmationStatus == null)
            confirmationStatus = "PENDING";
        if (acceptedCount == null)
            acceptedCount = 0;
        if (pendingMatchCount == null)
            pendingMatchCount = 0;
    }
}
