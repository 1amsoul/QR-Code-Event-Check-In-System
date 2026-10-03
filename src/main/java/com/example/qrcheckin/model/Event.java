package com.example.qrcheckin.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Instant eventDate;

    // Random token embedded in the QR code. Attendees can only check in if they
    // present this exact token, so a guessed/incremented event id alone is useless.
    @Column(nullable = false, unique = true, updatable = false)
    private String qrToken;

    @Column(nullable = false)
    private Instant createdAt;

    public Event(String name, Instant eventDate) {
        this.name = name;
        this.eventDate = eventDate;
        this.qrToken = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
    }
}
