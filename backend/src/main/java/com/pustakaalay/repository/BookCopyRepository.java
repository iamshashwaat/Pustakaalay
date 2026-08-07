package com.pustakaalay.repository;

import com.pustakaalay.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    Optional<BookCopy> findByBarcode(String barcode);

    boolean existsByBarcode(String barcode);

    List<BookCopy> findByBookId(Long bookId);

    List<BookCopy> findByStatus(BookCopy.CopyStatus status);

    long countByBookId(Long bookId);

    long countByBookIdAndStatus(
            Long bookId,
            BookCopy.CopyStatus status
    );
}
