package com.pustakaalay.controller;

import com.pustakaalay.dto.BookCopyRequest;
import com.pustakaalay.entity.BookCopy;
import com.pustakaalay.service.BookCopyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book-copies")
public class BookCopyController {

    private final BookCopyService bookCopyService;

    public BookCopyController(BookCopyService bookCopyService) {
        this.bookCopyService = bookCopyService;
    }

    @PostMapping
    public ResponseEntity<BookCopy> createBookCopy(
            @Valid @RequestBody BookCopyRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookCopyService.createBookCopy(request));
    }

    @GetMapping
    public ResponseEntity<List<BookCopy>> getAllBookCopies() {
        return ResponseEntity.ok(
                bookCopyService.getAllBookCopies()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookCopy> getBookCopyById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                bookCopyService.getBookCopyById(id)
        );
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<BookCopy> getByBarcode(
            @PathVariable String barcode
    ) {
        return ResponseEntity.ok(
                bookCopyService.getByBarcode(barcode)
        );
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BookCopy>> getCopiesByBookId(
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(
                bookCopyService.getCopiesByBookId(bookId)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookCopy>> getCopiesByStatus(
            @PathVariable BookCopy.CopyStatus status
    ) {
        return ResponseEntity.ok(
                bookCopyService.getCopiesByStatus(status)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookCopy> updateBookCopy(
            @PathVariable Long id,
            @Valid @RequestBody BookCopyRequest request
    ) {
        return ResponseEntity.ok(
                bookCopyService.updateBookCopy(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookCopy(
            @PathVariable Long id
    ) {
        bookCopyService.deleteBookCopy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/book/{bookId}/count")
    public ResponseEntity<Long> countCopiesByBook(
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(
                bookCopyService.countCopiesByBook(bookId)
        );
    }

    @GetMapping("/book/{bookId}/available-count")
    public ResponseEntity<Long> countAvailableCopiesByBook(
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(
                bookCopyService.countAvailableCopiesByBook(bookId)
        );
    }
}
