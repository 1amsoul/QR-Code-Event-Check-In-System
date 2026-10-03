package com.example.qrcheckin.repository;

import com.example.qrcheckin.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByQrToken(String qrToken);
}
