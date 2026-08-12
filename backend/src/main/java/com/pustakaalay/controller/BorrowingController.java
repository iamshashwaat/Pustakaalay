package com.pustakaalay.controller;

import com.pustakaalay.dto.BorrowRequest;
import com.pustakaalay.dto.BorrowingResponse;
import com.pustakaalay.entity.Borrowing;
import com.pustakaalay.exception.ResourceNotFoundException;
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
    public ResponseEntity<BorrowingResponse> issueBook(
            @Valid @RequestBody BorrowRequest request
    ) {
        Borrowing borrowing = borrowingService.issueBook(
                request.getUserId(),
                request.getBookCopyId(),
                request.getDueAt()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BorrowingResponse(borrowing));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<BorrowingResponse> returnBook(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                new BorrowingResponse(
                        borrowingService.returnBook(id)
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<BorrowingResponse>> getAllBorrowings() {
        return ResponseEntity.ok(
                borrowingRepository.findAll()
                        .stream()
                        .map(BorrowingResponse::new)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingResponse> getBorrowingById(
            @PathVariable Long id
    ) {
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Borrowing not found with id: " + id
                        )
                );

        return ResponseEntity.ok(
                new BorrowingResponse(borrowing)
        );
    }
}
