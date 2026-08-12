package com.pustakaalay.controller;

import com.pustakaalay.dto.FineResponse;
import com.pustakaalay.entity.Fine;
import com.pustakaalay.entity.User;
import com.pustakaalay.exception.ResourceNotFoundException;
import com.pustakaalay.repository.UserRepository;
import com.pustakaalay.service.FineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;
    private final UserRepository userRepository;

    public FineController(
            FineService fineService,
            UserRepository userRepository
    ) {
        this.fineService = fineService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<FineResponse>> getAll(
            Authentication authentication
    ) {
        List<Fine> fines;

        if (isAdmin(authentication)) {
            fines = fineService.getAll();
        } else {
            User currentUser = getCurrentUser(authentication);

            fines = fineService.getByUser(
                    currentUser.getId()
            );
        }

        return ResponseEntity.ok(
                fines.stream()
                        .map(FineResponse::new)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FineResponse> getById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        Fine fine = fineService.getById(id);

        if (!isAdmin(authentication)) {
            User currentUser = getCurrentUser(authentication);

            if (!fine.getUser().getId()
                    .equals(currentUser.getId())) {

                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "You cannot access another user's fine"
                );
            }
        }

        return ResponseEntity.ok(
                new FineResponse(fine)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FineResponse>> getByUser(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        Long permittedUserId = resolvePermittedUserId(
                userId,
                authentication
        );

        return ResponseEntity.ok(
                fineService.getByUser(permittedUserId)
                        .stream()
                        .map(FineResponse::new)
                        .toList()
        );
    }

    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<List<FineResponse>> getPendingByUser(
            @PathVariable Long userId,
            Authentication authentication
    ) {
        Long permittedUserId = resolvePermittedUserId(
                userId,
                authentication
        );

        return ResponseEntity.ok(
                fineService.getPendingByUser(permittedUserId)
                        .stream()
                        .map(FineResponse::new)
                        .toList()
        );
    }

    @PostMapping("/borrowing/{borrowingId}")
    public ResponseEntity<FineResponse> createForBorrowing(
            @PathVariable Long borrowingId
    ) {
        return ResponseEntity.ok(
                new FineResponse(
                        fineService.createFineForBorrowing(
                                borrowingId
                        )
                )
        );
    }

    @PostMapping("/{id}/paid")
    public ResponseEntity<FineResponse> markPaid(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                new FineResponse(
                        fineService.markPaid(id)
                )
        );
    }

    @PostMapping("/{id}/waive")
    public ResponseEntity<FineResponse> waive(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                new FineResponse(
                        fineService.waive(id)
                )
        );
    }

    @PostMapping("/process-overdue")
    public ResponseEntity<Integer> processOverdue() {
        return ResponseEntity.ok(
                fineService.markOverdueBorrowings()
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

    private Long resolvePermittedUserId(
            Long requestedUserId,
            Authentication authentication
    ) {
        if (isAdmin(authentication)) {
            return requestedUserId;
        }

        User currentUser = getCurrentUser(authentication);

        if (!currentUser.getId().equals(requestedUserId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot access another user's fines"
            );
        }

        return currentUser.getId();
    }
}
