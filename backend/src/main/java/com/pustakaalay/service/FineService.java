package com.pustakaalay.service;

import com.pustakaalay.entity.Borrowing;
import com.pustakaalay.entity.Fine;
import com.pustakaalay.exception.ConflictException;
import com.pustakaalay.exception.ResourceNotFoundException;
import com.pustakaalay.repository.BorrowingRepository;
import com.pustakaalay.repository.FineRepository;
import com.pustakaalay.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FineService {

    private static final BigDecimal FINE_PER_DAY =
            new BigDecimal("10.00");

    private final FineRepository fineRepository;
    private final BorrowingRepository borrowingRepository;
    private final UserRepository userRepository;

    public FineService(
            FineRepository fineRepository,
            BorrowingRepository borrowingRepository,
            UserRepository userRepository
    ) {
        this.fineRepository = fineRepository;
        this.borrowingRepository = borrowingRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Fine createFineForBorrowing(Long borrowingId) {

        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Borrowing not found with id: " + borrowingId
                        )
                );

        if (fineRepository.existsByBorrowingId(borrowingId)) {
            throw new ConflictException(
                    "Fine already exists for borrowing id: " + borrowingId
            );
        }

        LocalDateTime endTime =
                borrowing.getReturnedAt() != null
                        ? borrowing.getReturnedAt()
                        : LocalDateTime.now();

        if (!endTime.isAfter(borrowing.getDueAt())) {
            throw new ConflictException(
                    "Borrowing is not overdue"
            );
        }

        long overdueDays = calculateOverdueDays(
                borrowing.getDueAt(),
                endTime
        );

        BigDecimal amount =
                FINE_PER_DAY.multiply(
                        BigDecimal.valueOf(overdueDays)
                );

        Fine fine = new Fine();
        fine.setUser(borrowing.getUser());
        fine.setBorrowing(borrowing);
        fine.setAmount(amount);
        fine.setReason(
                "Overdue by " + overdueDays +
                        (overdueDays == 1 ? " day" : " days")
        );
        fine.setStatus(Fine.FineStatus.PENDING);

        return fineRepository.save(fine);
    }

    @Transactional
    public int markOverdueBorrowings() {

        List<Borrowing> borrowings =
                borrowingRepository.findByStatus(
                        Borrowing.BorrowingStatus.BORROWED
                );

        int updated = 0;
        LocalDateTime now = LocalDateTime.now();

        for (Borrowing borrowing : borrowings) {
            if (borrowing.getDueAt().isBefore(now)) {
                borrowing.setStatus(
                        Borrowing.BorrowingStatus.OVERDUE
                );
                borrowingRepository.save(borrowing);
                updated++;
            }
        }

        return updated;
    }

    @Transactional(readOnly = true)
    public List<Fine> getAll() {
        return fineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Fine getById(Long id) {
        return fineRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Fine not found with id: " + id
                        )
                );
    }

    @Transactional(readOnly = true)
    public List<Fine> getByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return fineRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Fine> getPendingByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return fineRepository.findByUserIdAndStatus(
                userId,
                Fine.FineStatus.PENDING
        );
    }

    @Transactional
    public Fine markPaid(Long fineId) {

        Fine fine = getById(fineId);

        if (fine.getStatus() != Fine.FineStatus.PENDING) {
            throw new ConflictException(
                    "Only pending fines can be marked as paid"
            );
        }

        fine.setStatus(Fine.FineStatus.PAID);
        fine.setPaidAt(LocalDateTime.now());

        return fineRepository.save(fine);
    }

    @Transactional
    public Fine waive(Long fineId) {

        Fine fine = getById(fineId);

        if (fine.getStatus() != Fine.FineStatus.PENDING) {
            throw new ConflictException(
                    "Only pending fines can be waived"
            );
        }

        fine.setStatus(Fine.FineStatus.WAIVED);
        fine.setPaidAt(null);

        return fineRepository.save(fine);
    }

    private long calculateOverdueDays(
            LocalDateTime dueAt,
            LocalDateTime endTime
    ) {

        long seconds =
                Duration.between(dueAt, endTime).getSeconds();

        long daySeconds = 24 * 60 * 60;

        return Math.max(
                1,
                (seconds + daySeconds - 1) / daySeconds
        );
    }
}
