package com.pustakaalay.dto;

import com.pustakaalay.entity.Borrowing;

import java.time.LocalDateTime;

public class BorrowingResponse {

    private final Long id;
    private final UserSummaryResponse user;
    private final BookCopyResponse bookCopy;
    private final LocalDateTime borrowedAt;
    private final LocalDateTime dueAt;
    private final LocalDateTime returnedAt;
    private final Borrowing.BorrowingStatus status;
    private final Integer renewalCount;
    private final String notes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public BorrowingResponse(Borrowing borrowing) {
        this.id = borrowing.getId();
        this.user = new UserSummaryResponse(borrowing.getUser());
        this.bookCopy = new BookCopyResponse(borrowing.getBookCopy());
        this.borrowedAt = borrowing.getBorrowedAt();
        this.dueAt = borrowing.getDueAt();
        this.returnedAt = borrowing.getReturnedAt();
        this.status = borrowing.getStatus();
        this.renewalCount = borrowing.getRenewalCount();
        this.notes = borrowing.getNotes();
        this.createdAt = borrowing.getCreatedAt();
        this.updatedAt = borrowing.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public UserSummaryResponse getUser() {
        return user;
    }

    public BookCopyResponse getBookCopy() {
        return bookCopy;
    }

    public LocalDateTime getBorrowedAt() {
        return borrowedAt;
    }

    public LocalDateTime getDueAt() {
        return dueAt;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public Borrowing.BorrowingStatus getStatus() {
        return status;
    }

    public Integer getRenewalCount() {
        return renewalCount;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
