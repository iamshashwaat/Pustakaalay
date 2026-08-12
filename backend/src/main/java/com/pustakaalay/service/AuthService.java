package com.pustakaalay.service;

import com.pustakaalay.dto.LoginRequest;
import com.pustakaalay.dto.LoginResponse;
import com.pustakaalay.entity.User;
import com.pustakaalay.exception.ConflictException;
import com.pustakaalay.repository.UserRepository;
import com.pustakaalay.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ConflictException("Invalid email or password")
                );

        if (user.getStatus() != User.UserStatus.ACTIVE) {
            throw new ConflictException(
                    "User account is not active"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash()
        )) {
            throw new ConflictException(
                    "Invalid email or password"
            );
        }

        user.setLastLoginAt(java.time.LocalDateTime.now());
        userRepository.save(user);

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().getName()
        );

        return new LoginResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getRole().getName()
        );
    }
}
