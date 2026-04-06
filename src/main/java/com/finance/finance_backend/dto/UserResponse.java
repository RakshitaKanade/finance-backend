package com.finance.finance_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.finance.finance_backend.model.Role;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Role role;
    private boolean active;
    private LocalDateTime createdAt;
}

