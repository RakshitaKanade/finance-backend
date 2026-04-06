package com.finance.finance_backend.service;

import com.finance.finance_backend.dto.LoginRequest;
import com.finance.finance_backend.dto.LoginResponse;
import com.finance.finance_backend.exception.ApiException;
import com.finance.finance_backend.model.User;
import com.finance.finance_backend.repository.UserRepository;
import com.finance.finance_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException(
                        "Invalid email or password", HttpStatus.UNAUTHORIZED));

        // Check if user is active
        if (!user.isActive()) {
            throw new ApiException(
                    "Account is inactive", HttpStatus.FORBIDDEN);
        }

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException(
                    "Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(
                user.getEmail(), user.getRole().name());

        return new LoginResponse(token, user.getEmail(), user.getRole().name());
    }
}
