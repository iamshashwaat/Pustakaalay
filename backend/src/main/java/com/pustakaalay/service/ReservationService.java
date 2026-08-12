package com.pustakaalay.service;

import com.pustakaalay.dto.ReservationRequest;
import com.pustakaalay.entity.Book;
import com.pustakaalay.entity.Reservation;
import com.pustakaalay.entity.User;
import com.pustakaalay.exception.ConflictException;
import com.pustakaalay.exception.ResourceNotFoundException;
import com.pustakaalay.repository.BookRepository;
import com.pustakaalay.repository.ReservationRepository;
import com.pustakaalay.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            UserRepository userRepository,
            BookRepository bookRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public Reservation createReservation(ReservationRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + request.getUserId()
                        )
                );

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Book not found with id: " + request.getBookId()
                        )
                );

        if (reservationRepository.existsByUser_IdAndBook_IdAndStatus(
                user.getId(),
                book.getId(),
                Reservation.ReservationStatus.ACTIVE
        )) {
            throw new ConflictException(
                    "User already has an active reservation for this book"
            );
        }

        long activeReservations =
                reservationRepository.countByBook_IdAndStatus(
                        book.getId(),
                        Reservation.ReservationStatus.ACTIVE
                );

        Reservation reservation = new Reservation();

        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setExpiresAt(request.getExpiresAt());
        reservation.setStatus(Reservation.ReservationStatus.ACTIVE);
        reservation.setQueuePosition((int) activeReservations + 1);

        return reservationRepository.save(reservation);
    }

    @Transactional(readOnly = true)
    public Reservation getById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reservation not found with id: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Reservation> getByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return reservationRepository
                .findByUser_IdOrderByReservedAtDesc(userId);
    }

    @Transactional
    public Reservation cancel(Long id) {

        Reservation reservation = getById(id);

        if (reservation.getStatus() !=
                Reservation.ReservationStatus.ACTIVE) {
            throw new ConflictException(
                    "Only active reservations can be cancelled"
            );
        }

        reservation.setStatus(
                Reservation.ReservationStatus.CANCELLED
        );

        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation fulfill(Long id) {

        Reservation reservation = getById(id);

        if (reservation.getStatus() !=
                Reservation.ReservationStatus.ACTIVE) {
            throw new ConflictException(
                    "Only active reservations can be fulfilled"
            );
        }

        reservation.setStatus(
                Reservation.ReservationStatus.FULFILLED
        );

        return reservationRepository.save(reservation);
    }
}
