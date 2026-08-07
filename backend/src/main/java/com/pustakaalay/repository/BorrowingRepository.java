package com.pustakaalay.repository;

import com.pustakaalay.entity.Borrowing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowingRepository extends JpaRepository<Borrowing, Long> {

    List<Borrowing> findByUserId(Long userId);

    List<Borrowing> findByUserIdAndStatus(
            Long userId,
            Borrowing.BorrowingStatus status
    );

    List<Borrowing> findByStatus(
            Borrowing.BorrowingStatus status
    );

    boolean existsByBookCopyIdAndStatus(
            Long bookCopyId,
            Borrowing.BorrowingStatus status
    );

    long countByUserIdAndStatus(
            Long userId,
            Borrowing.BorrowingStatus status
    );
}
