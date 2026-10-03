package com.example.qrcheckin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(
    name = "check_ins",
    // One check-in per attendee email per event - prevents the same person
    // scanning twice and double-counting attendance.
    uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "attendee_email"})
)
@Getter
@Setter
@NoArgsConstructor
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String attendeeName;

    @Column(name = "attendee_email", nullable = false)
    private String attendeeEmail;

    @Column(nullable = false)
    private Instant checkedInAt;

    public CheckIn(Event event, String attendeeName, String attendeeEmail) {
        this.event = event;
        this.attendeeName = attendeeName;
        this.attendeeEmail = attendeeEmail;
        this.checkedInAt = Instant.now();
    }
}
