package com.pustakaalay.controller;

import com.pustakaalay.dto.FineResponse;
import com.pustakaalay.service.FineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fines")
public class FineController {

    private final FineService fineService;

    public FineController(FineService fineService) {
        this.fineService = fineService;
    }

    @GetMapping
    public ResponseEntity<List<FineResponse>> getAll() {
        return ResponseEntity.ok(
                fineService.getAll()
                        .stream()
                        .map(FineResponse::new)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FineResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                new FineResponse(
                        fineService.getById(id)
                )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FineResponse>> getByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                fineService.getByUser(userId)
                        .stream()
                        .map(FineResponse::new)
                        .toList()
        );
    }

    @GetMapping("/user/{userId}/pending")
    public ResponseEntity<List<FineResponse>> getPendingByUser(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                fineService.getPendingByUser(userId)
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
}
