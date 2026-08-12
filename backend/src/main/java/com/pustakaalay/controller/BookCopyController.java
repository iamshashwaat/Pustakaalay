package com.pustakaalay.controller;

import com.pustakaalay.dto.BookCopyRequest;
import com.pustakaalay.dto.BookCopyResponse;
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
    public ResponseEntity<BookCopyResponse> createBookCopy(
            @Valid @RequestBody BookCopyRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BookCopyResponse(
                        bookCopyService.createBookCopy(request)
                ));
    }

    @GetMapping
    public ResponseEntity<List<BookCopyResponse>> getAllBookCopies() {
        return ResponseEntity.ok(
                bookCopyService.getAllBookCopies()
                        .stream()
                        .map(BookCopyResponse::new)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookCopyResponse> getBookCopyById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                new BookCopyResponse(
                        bookCopyService.getBookCopyById(id)
                )
        );
    }

    @GetMapping("/barcode/{barcode}")
    public ResponseEntity<BookCopyResponse> getByBarcode(
            @PathVariable String barcode
    ) {
        return ResponseEntity.ok(
                new BookCopyResponse(
                        bookCopyService.getByBarcode(barcode)
                )
        );
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<BookCopyResponse>> getCopiesByBookId(
            @PathVariable Long bookId
    ) {
        return ResponseEntity.ok(
                bookCopyService.getCopiesByBookId(bookId)
                        .stream()
                        .map(BookCopyResponse::new)
                        .toList()
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<BookCopyResponse>> getCopiesByStatus(
            @PathVariable BookCopy.CopyStatus status
    ) {
        return ResponseEntity.ok(
                bookCopyService.getCopiesByStatus(status)
                        .stream()
                        .map(BookCopyResponse::new)
                        .toList()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookCopyResponse> updateBookCopy(
            @PathVariable Long id,
            @Valid @RequestBody BookCopyRequest request
    ) {
        return ResponseEntity.ok(
                new BookCopyResponse(
                        bookCopyService.updateBookCopy(id, request)
                )
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
