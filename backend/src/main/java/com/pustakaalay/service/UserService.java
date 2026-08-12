package com.pustakaalay.service;

import com.pustakaalay.dto.UserRequest;
import com.pustakaalay.entity.Role;
import com.pustakaalay.entity.User;
import com.pustakaalay.exception.ConflictException;
import com.pustakaalay.exception.ResourceNotFoundException;
import com.pustakaalay.repository.RoleRepository;
import com.pustakaalay.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public User create(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(
                    "User with email already exists: " + request.getEmail()
            );
        }

        if (request.getMembershipNumber() != null &&
                userRepository.existsByMembershipNumber(
                        request.getMembershipNumber()
                )) {
            throw new ConflictException(
                    "Membership number already exists: " +
                            request.getMembershipNumber()
            );
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found with id: " + request.getRoleId()
                        )
                );

        User user = new User();

        apply(user, request, role);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );
    }

    @Transactional
    public User update(Long id, UserRequest request) {

        User user = getById(id);

        userRepository.findByEmail(request.getEmail())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException(
                            "User with email already exists: " +
                                    request.getEmail()
                    );
                });

        if (request.getMembershipNumber() != null) {
            userRepository.findAll()
                    .stream()
                    .filter(existing ->
                            request.getMembershipNumber()
                                    .equals(existing.getMembershipNumber())
                    )
                    .filter(existing -> !existing.getId().equals(id))
                    .findFirst()
                    .ifPresent(existing -> {
                        throw new ConflictException(
                                "Membership number already exists: " +
                                        request.getMembershipNumber()
                        );
                    });
        }

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Role not found with id: " +
                                        request.getRoleId()
                        )
                );

        apply(user, request, role);

        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(getById(id));
    }

    private void apply(
            User user,
            UserRequest request,
            Role role
    ) {
        user.setRole(role);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        // Temporary plain storage until authentication module is added.
        user.setPasswordHash(request.getPassword());

        user.setPhone(request.getPhone());
        user.setMembershipNumber(request.getMembershipNumber());

        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }
    }
}
