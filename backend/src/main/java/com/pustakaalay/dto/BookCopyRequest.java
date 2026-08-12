package com.pustakaalay.dto;

import com.pustakaalay.entity.BookCopy;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookCopyRequest {

    @NotNull(message = "Book ID is required")
    private Long bookId;

    @NotBlank(message = "Barcode is required")
    @Size(max = 50, message = "Barcode must not exceed 50 characters")
    private String barcode;

    private LocalDate acquisitionDate;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    private BigDecimal price;

    private BookCopy.CopyStatus status;

    private BookCopy.ConditionStatus conditionStatus;

    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    public BookCopyRequest() {
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BookCopy.CopyStatus getStatus() {
        return status;
    }

    public void setStatus(BookCopy.CopyStatus status) {
        this.status = status;
    }

    public BookCopy.ConditionStatus getConditionStatus() {
        return conditionStatus;
    }

    public void setConditionStatus(BookCopy.ConditionStatus conditionStatus) {
        this.conditionStatus = conditionStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
