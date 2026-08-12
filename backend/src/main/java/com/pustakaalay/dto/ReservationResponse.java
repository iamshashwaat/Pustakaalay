package com.pustakaalay.dto;

import com.pustakaalay.entity.Reservation;

import java.time.LocalDateTime;

public class ReservationResponse {

    private final Long id;
    private final Long userId;
    private final Long bookId;
    private final String bookTitle;
    private final LocalDateTime reservedAt;
    private final LocalDateTime expiresAt;
    private final Reservation.ReservationStatus status;
    private final Integer queuePosition;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.userId = reservation.getUser().getId();
        this.bookId = reservation.getBook().getId();
        this.bookTitle = reservation.getBook().getTitle();
        this.reservedAt = reservation.getReservedAt();
        this.expiresAt = reservation.getExpiresAt();
        this.status = reservation.getStatus();
        this.queuePosition = reservation.getQueuePosition();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDateTime getReservedAt() {
        return reservedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public Reservation.ReservationStatus getStatus() {
        return status;
    }

    public Integer getQueuePosition() {
        return queuePosition;
    }
}
