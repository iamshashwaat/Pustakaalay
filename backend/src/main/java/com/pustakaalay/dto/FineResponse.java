package com.pustakaalay.dto;

import com.pustakaalay.entity.Fine;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FineResponse {

    private final Long id;
    private final Long userId;
    private final Long borrowingId;
    private final BigDecimal amount;
    private final String reason;
    private final Fine.FineStatus status;
    private final LocalDateTime issuedAt;
    private final LocalDateTime paidAt;

    public FineResponse(Fine fine) {
        this.id = fine.getId();
        this.userId = fine.getUser().getId();
        this.borrowingId = fine.getBorrowing().getId();
        this.amount = fine.getAmount();
        this.reason = fine.getReason();
        this.status = fine.getStatus();
        this.issuedAt = fine.getIssuedAt();
        this.paidAt = fine.getPaidAt();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getBorrowingId() { return borrowingId; }
    public BigDecimal getAmount() { return amount; }
    public String getReason() { return reason; }
    public Fine.FineStatus getStatus() { return status; }
    public LocalDateTime getIssuedAt() { return issuedAt; }
    public LocalDateTime getPaidAt() { return paidAt; }
}
