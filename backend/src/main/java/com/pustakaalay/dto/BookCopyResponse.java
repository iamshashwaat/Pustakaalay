package com.pustakaalay.dto;

import com.pustakaalay.entity.BookCopy;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookCopyResponse {

    private final Long id;
    private final BookSummaryResponse book;
    private final String barcode;
    private final LocalDate acquisitionDate;
    private final BigDecimal price;
    private final BookCopy.CopyStatus status;
    private final BookCopy.ConditionStatus conditionStatus;
    private final String location;
    private final LocalDateTime createdAt;

    public BookCopyResponse(BookCopy copy) {
        this.id = copy.getId();
        this.book = new BookSummaryResponse(copy.getBook());
        this.barcode = copy.getBarcode();
        this.acquisitionDate = copy.getAcquisitionDate();
        this.price = copy.getPrice();
        this.status = copy.getStatus();
        this.conditionStatus = copy.getConditionStatus();
        this.location = copy.getLocation();
        this.createdAt = copy.getCreatedAt();
    }

    public Long getId() { return id; }
    public BookSummaryResponse getBook() { return book; }
    public String getBarcode() { return barcode; }
    public LocalDate getAcquisitionDate() { return acquisitionDate; }
    public BigDecimal getPrice() { return price; }
    public BookCopy.CopyStatus getStatus() { return status; }
    public BookCopy.ConditionStatus getConditionStatus() { return conditionStatus; }
    public String getLocation() { return location; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
