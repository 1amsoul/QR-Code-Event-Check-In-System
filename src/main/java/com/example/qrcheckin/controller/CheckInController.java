package com.example.qrcheckin.controller;

import com.example.qrcheckin.dto.Dtos.*;
import com.example.qrcheckin.model.CheckIn;
import com.example.qrcheckin.service.CheckInService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkin")
public class CheckInController {

    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping
    public ResponseEntity<?> checkIn(@Valid @RequestBody CheckInRequest request) {
        try {
            CheckIn checkIn = checkInService.checkIn(request.token(), request.attendeeName(), request.attendeeEmail());
            CheckInResponse response = new CheckInResponse(
                    checkIn.getId(),
                    checkIn.getEvent().getName(),
                    checkIn.getAttendeeName(),
                    checkIn.getAttendeeEmail(),
                    checkIn.getCheckedInAt()
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
}
