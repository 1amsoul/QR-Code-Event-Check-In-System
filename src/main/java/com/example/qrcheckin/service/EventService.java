package com.example.qrcheckin.service;

import com.example.qrcheckin.model.Event;
import com.example.qrcheckin.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(String name, Instant eventDate) {
        Event event = new Event(name, eventDate);
        return eventRepository.save(event);
    }

    public Event getById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Event not found: " + id));
    }

    public Event getByToken(String token) {
        return eventRepository.findByQrToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or unknown QR token"));
    }

    public List<Event> getAll() {
        return eventRepository.findAll();
    }
}
