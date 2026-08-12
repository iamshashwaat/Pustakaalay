package com.pustakaalay.repository;

import com.pustakaalay.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {

    boolean existsByUser_IdAndBook_IdAndStatus(
            Long userId,
            Long bookId,
            Reservation.ReservationStatus status
    );

    long countByBook_IdAndStatus(
            Long bookId,
            Reservation.ReservationStatus status
    );

    List<Reservation> findByUser_IdOrderByReservedAtDesc(Long userId);

    List<Reservation> findByBook_IdAndStatusOrderByQueuePositionAsc(
            Long bookId,
            Reservation.ReservationStatus status
    );
}
