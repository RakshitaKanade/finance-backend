package com.finance.finance_backend.service;

import com.finance.finance_backend.dto.UserRequest;
import com.finance.finance_backend.dto.UserResponse;
import com.finance.finance_backend.exception.ApiException;
import com.finance.finance_backend.model.User;
import com.finance.finance_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Get all users
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Get single user by id
    public UserResponse getUserById(Long id) {
        User user = findUserById(id);
        return toResponse(user);
    }

    // Create new user
    public UserResponse createUser(UserRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(
                    "Email already exists", HttpStatus.CONFLICT);
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .active(true)
                .build();

        return toResponse(userRepository.save(user));
    }

    // Update user
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = findUserById(id);

        // If email is being changed, check it's not taken
        if (!user.getEmail().equals(request.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new ApiException(
                    "Email already exists", HttpStatus.CONFLICT);
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        return toResponse(userRepository.save(user));
    }

    // Toggle user active/inactive status
    public UserResponse toggleUserStatus(Long id) {
        User user = findUserById(id);
        user.setActive(!user.isActive());
        return toResponse(userRepository.save(user));
    }

    // Delete user
    public void deleteUser(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    // Helper: find user or throw 404
    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        "User not found", HttpStatus.NOT_FOUND));
    }

    // Helper: convert User model to UserResponse DTO
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}
