package com.pustakaalay.controller;

import com.pustakaalay.dto.RoleRequest;
import com.pustakaalay.dto.RoleResponse;
import com.pustakaalay.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RoleResponse> create(
            @Valid @RequestBody RoleRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RoleResponse(
                        roleService.create(request)
                ));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAll() {
        return ResponseEntity.ok(
                roleService.getAll()
                        .stream()
                        .map(RoleResponse::new)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                new RoleResponse(roleService.getById(id))
        );
    }
}
