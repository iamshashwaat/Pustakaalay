package com.pustakaalay.controller;

import com.pustakaalay.dto.BorrowRequest;
import com.pustakaalay.entity.Borrowing;
import com.pustakaalay.repository.BorrowingRepository;
import com.pustakaalay.service.BorrowingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;
    private final BorrowingRepository borrowingRepository;

    public BorrowingController(
            BorrowingService borrowingService,
            BorrowingRepository borrowingRepository
    ) {
        this.borrowingService = borrowingService;
        this.borrowingRepository = borrowingRepository;
    }

    @PostMapping("/issue")
    public ResponseEntity<Borrowing> issueBook(
            @Valid @RequestBody BorrowRequest request
    ) {
        Borrowing borrowing = borrowingService.issueBook(
                request.getUserId(),
                request.getBookCopyId(),
                request.getDueAt()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(borrowing);
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Borrowing> returnBook(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                borrowingService.returnBook(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<Borrowing>> getAllBorrowings() {
        return ResponseEntity.ok(
                borrowingRepository.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrowing> getBorrowingById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                borrowingRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Borrowing not found with id: " + id
                                )
                        )
        );
    }
}
