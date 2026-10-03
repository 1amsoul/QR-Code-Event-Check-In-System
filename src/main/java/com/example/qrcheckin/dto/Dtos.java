package com.example.qrcheckin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public class Dtos {

    public record CreateEventRequest(
            @NotBlank String name,
            @NotBlank String eventDate // ISO-8601, e.g. 2026-10-10T09:00:00Z
    ) {}

    public record EventResponse(
            Long id,
            String name,
            Instant eventDate,
            String qrToken,
            Instant createdAt
    ) {}

    public record CheckInRequest(
            @NotBlank String token,
            @NotBlank String attendeeName,
            @NotBlank @Email String attendeeEmail
    ) {}

    public record CheckInResponse(
            Long id,
            String eventName,
            String attendeeName,
            String attendeeEmail,
            Instant checkedInAt
    ) {}

    public record ErrorResponse(String message) {}
}
