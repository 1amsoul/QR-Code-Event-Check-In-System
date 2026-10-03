package com.example.qrcheckin.controller;

import com.example.qrcheckin.dto.Dtos.*;
import com.example.qrcheckin.model.CheckIn;
import com.example.qrcheckin.model.Event;
import com.example.qrcheckin.service.CheckInService;
import com.example.qrcheckin.service.EventService;
import com.example.qrcheckin.service.QrCodeGenerator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final CheckInService checkInService;
    private final QrCodeGenerator qrCodeGenerator;

    public EventController(EventService eventService, CheckInService checkInService, QrCodeGenerator qrCodeGenerator) {
        this.eventService = eventService;
        this.checkInService = checkInService;
        this.qrCodeGenerator = qrCodeGenerator;
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@Valid @RequestBody CreateEventRequest request) {
        Event event = eventService.createEvent(request.name(), Instant.parse(request.eventDate()));
        return new ResponseEntity<>(toResponse(event), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAll() {
        List<EventResponse> events = eventService.getAll().stream().map(this::toResponse).toList();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(eventService.getById(id)));
    }

    // Returns a scannable PNG QR code that encodes this event's check-in link.
    @GetMapping(value = "/{id}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getQrCode(@PathVariable Long id) throws Exception {
        Event event = eventService.getById(id);
        String checkInUrl = "/checkin.html?token=" + event.getQrToken();
        byte[] png = qrCodeGenerator.generatePng(checkInUrl);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(png);
    }

    @GetMapping("/{id}/checkins")
    public ResponseEntity<List<CheckInResponse>> getCheckIns(@PathVariable Long id) {
        Event event = eventService.getById(id);
        List<CheckInResponse> checkIns = checkInService.getCheckInsForEvent(id).stream()
                .map(c -> new CheckInResponse(c.getId(), event.getName(), c.getAttendeeName(), c.getAttendeeEmail(), c.getCheckedInAt()))
                .toList();
        return ResponseEntity.ok(checkIns);
    }

    private EventResponse toResponse(Event event) {
        return new EventResponse(event.getId(), event.getName(), event.getEventDate(), event.getQrToken(), event.getCreatedAt());
    }
}
