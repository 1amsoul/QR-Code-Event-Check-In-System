package com.example.qrcheckin.service;

import com.example.qrcheckin.model.CheckIn;
import com.example.qrcheckin.model.Event;
import com.example.qrcheckin.repository.CheckInRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final EventService eventService;

    public CheckInService(CheckInRepository checkInRepository, EventService eventService) {
        this.checkInRepository = checkInRepository;
        this.eventService = eventService;
    }

    @Transactional
    public CheckIn checkIn(String qrToken, String attendeeName, String attendeeEmail) {
        Event event = eventService.getByToken(qrToken);

        // Belt-and-braces duplicate check in addition to the DB unique constraint,
        // so we can return a clear message instead of a raw SQL exception.
        checkInRepository.findByEventIdAndAttendeeEmail(event.getId(), attendeeEmail)
                .ifPresent(existing -> {
                    throw new IllegalStateException(
                            "This email has already checked in to \"" + event.getName() + "\" at "
                                    + existing.getCheckedInAt());
                });

        try {
            CheckIn checkIn = new CheckIn(event, attendeeName, attendeeEmail);
            return checkInRepository.save(checkIn);
        } catch (DataIntegrityViolationException e) {
            // Race condition fallback: two near-simultaneous scans for the same
            // email both pass the check above, DB unique constraint catches it.
            throw new IllegalStateException("This email has already checked in to this event.");
        }
    }

    public List<CheckIn> getCheckInsForEvent(Long eventId) {
        return checkInRepository.findByEventIdOrderByCheckedInAtDesc(eventId);
    }

    public long countForEvent(Long eventId) {
        return checkInRepository.countByEventId(eventId);
    }
}
