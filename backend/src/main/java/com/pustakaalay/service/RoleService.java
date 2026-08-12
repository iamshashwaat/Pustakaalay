package com.pustakaalay.service;

import com.pustakaalay.dto.RoleRequest;
import com.pustakaalay.entity.Role;
import com.pustakaalay.exception.ConflictException;
import com.pustakaalay.exception.ResourceNotFoundException;
import com.pustakaalay.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Role create(RoleRequest request) {

        roleRepository.findByName(request.getName())
                .ifPresent(role -> {
                    throw new ConflictException(
                            "Role already exists: " + request.getName()
                    );
                });

        Role role = new Role(
                request.getName(),
                request.getDescription()
        );

        return roleRepository.save(role);
    }

    @Transactional(readOnly = true)
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Role getById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found with id: " + id
                        )
                );
    }
}
