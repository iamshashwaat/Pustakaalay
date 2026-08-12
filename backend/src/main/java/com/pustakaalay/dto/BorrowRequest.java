package com.pustakaalay.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class BorrowRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Book copy ID is required")
    private Long bookCopyId;

    @NotNull(message = "Due date is required")
    private LocalDateTime dueAt;

    public BorrowRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookCopyId() {
        return bookCopyId;
    }

    public void setBookCopyId(Long bookCopyId) {
        this.bookCopyId = bookCopyId;
    }

    public LocalDateTime getDueAt() {
        return dueAt;
    }

    public void setDueAt(LocalDateTime dueAt) {
        this.dueAt = dueAt;
    }
}
