package com.pustakaalay.controller;

import com.pustakaalay.dto.ReservationRequest;
import com.pustakaalay.dto.ReservationResponse;
import com.pustakaalay.entity.Reservation;
import com.pustakaalay.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(
            ReservationService reservationService
    ) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationResponse> create(
            @Valid @RequestBody ReservationRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ReservationResponse(
                        reservationService.createReservation(request)
                ));
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> getAll() {
        return ResponseEntity.ok(
                reservationService.getAll()
                        .stream()
                        .map(ReservationResponse::new)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                new ReservationResponse(
                        reservationService.getById(id)
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponse>> getByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                reservationService.getByUser(userId)
                        .stream()
                        .map(ReservationResponse::new)
                        .toList()
        );
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponse> cancel(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                new ReservationResponse(
                        reservationService.cancel(id)
                )
        );
    }

    @PostMapping("/{id}/fulfill")
    public ResponseEntity<ReservationResponse> fulfill(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                new ReservationResponse(
                        reservationService.fulfill(id)
                )
        );
    }
}
