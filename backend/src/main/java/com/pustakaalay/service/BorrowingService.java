package com.pustakaalay.service;

import com.pustakaalay.entity.BookCopy;
import com.pustakaalay.entity.Borrowing;
import com.pustakaalay.entity.User;
import com.pustakaalay.repository.BookCopyRepository;
import com.pustakaalay.repository.BorrowingRepository;
import com.pustakaalay.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BorrowingService {

    private final BorrowingRepository borrowingRepository;
    private final BookCopyRepository bookCopyRepository;
    private final UserRepository userRepository;

    public BorrowingService(
            BorrowingRepository borrowingRepository,
            BookCopyRepository bookCopyRepository,
            UserRepository userRepository
    ) {
        this.borrowingRepository = borrowingRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Borrowing issueBook(
            Long userId,
            Long bookCopyId,
            LocalDateTime dueAt
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Book copy not found")
                );

        if (bookCopy.getStatus() != BookCopy.CopyStatus.AVAILABLE) {
            throw new IllegalStateException(
                    "Book copy is not available"
            );
        }

        Borrowing borrowing = new Borrowing();

        borrowing.setUser(user);
        borrowing.setBookCopy(bookCopy);
        borrowing.setBorrowedAt(LocalDateTime.now());
        borrowing.setDueAt(dueAt);
        borrowing.setStatus(Borrowing.BorrowingStatus.BORROWED);
        borrowing.setRenewalCount(0);

        bookCopy.setStatus(BookCopy.CopyStatus.BORROWED);

        bookCopyRepository.save(bookCopy);

        return borrowingRepository.save(borrowing);
    }

    @Transactional
    public Borrowing returnBook(Long borrowingId) {

        Borrowing borrowing = borrowingRepository.findById(borrowingId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Borrowing not found")
                );

        if (borrowing.getStatus() != Borrowing.BorrowingStatus.BORROWED &&
            borrowing.getStatus() != Borrowing.BorrowingStatus.OVERDUE) {

            throw new IllegalStateException(
                    "This borrowing cannot be returned"
            );
        }

        borrowing.setReturnedAt(LocalDateTime.now());
        borrowing.setStatus(Borrowing.BorrowingStatus.RETURNED);

        BookCopy bookCopy = borrowing.getBookCopy();
        bookCopy.setStatus(BookCopy.CopyStatus.AVAILABLE);

        bookCopyRepository.save(bookCopy);

        return borrowingRepository.save(borrowing);
    }
}
