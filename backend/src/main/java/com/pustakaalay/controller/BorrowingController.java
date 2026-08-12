package com.pustakaalay.controller;

import com.pustakaalay.dto.BorrowRequest;
import com.pustakaalay.dto.BorrowingResponse;
import com.pustakaalay.entity.Borrowing;
import com.pustakaalay.entity.User;
import com.pustakaalay.exception.ResourceNotFoundException;
import com.pustakaalay.repository.BorrowingRepository;
import com.pustakaalay.repository.UserRepository;
import com.pustakaalay.service.BorrowingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
public class BorrowingController {

    private final BorrowingService borrowingService;
    private final BorrowingRepository borrowingRepository;
    private final UserRepository userRepository;

    public BorrowingController(
            BorrowingService borrowingService,
            BorrowingRepository borrowingRepository,
            UserRepository userRepository
    ) {
        this.borrowingService = borrowingService;
        this.borrowingRepository = borrowingRepository;
        this.userRepository = userRepository;
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
    public ResponseEntity<List<BorrowingResponse>> getBorrowings(
            Authentication authentication
    ) {
        List<Borrowing> borrowings;

        if (isAdmin(authentication)) {
            borrowings = borrowingRepository.findAll();
        } else {
            User currentUser = getCurrentUser(authentication);

            borrowings = borrowingRepository.findByUserId(
                    currentUser.getId()
            );
        }

        return ResponseEntity.ok(
                borrowings.stream()
                        .map(BorrowingResponse::new)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingResponse> getBorrowingById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Borrowing borrowing = borrowingRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Borrowing not found with id: " + id
                        )
                );

        if (!isAdmin(authentication)) {
            User currentUser = getCurrentUser(authentication);

            if (!borrowing.getUser().getId()
                    .equals(currentUser.getId())) {

                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You cannot access another user's borrowing"
                );
            }
        }

        return ResponseEntity.ok(
                new BorrowingResponse(borrowing)
        );
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_ADMIN")
                );
    }

    private User getCurrentUser(Authentication authentication) {
        return userRepository.findByEmail(
                authentication.getName()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Authenticated user not found"
                )
        );
    }
}
