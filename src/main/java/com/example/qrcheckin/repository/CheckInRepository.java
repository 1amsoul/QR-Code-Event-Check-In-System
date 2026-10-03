package com.example.qrcheckin.repository;

import com.example.qrcheckin.model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    List<CheckIn> findByEventIdOrderByCheckedInAtDesc(Long eventId);
    Optional<CheckIn> findByEventIdAndAttendeeEmail(Long eventId, String attendeeEmail);
    long countByEventId(Long eventId);
}
