package com.pustakaalay.repository;

import com.pustakaalay.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FineRepository extends JpaRepository<Fine, Long> {

    Optional<Fine> findByBorrowingId(Long borrowingId);

    boolean existsByBorrowingId(Long borrowingId);

    List<Fine> findByUserId(Long userId);

    List<Fine> findByStatus(Fine.FineStatus status);

    List<Fine> findByUserIdAndStatus(
            Long userId,
            Fine.FineStatus status
    );
}
