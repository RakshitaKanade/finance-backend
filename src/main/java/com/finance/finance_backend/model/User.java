package com.finance.finance_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data                  // Lombok: generates getters, setters, toString
@Builder               // Lombok: lets us do User.builder().name("x").build()
@NoArgsConstructor     // Lombok: generates empty constructor
@AllArgsConstructor    // Lombok: generates constructor with all fields
@Entity                // JPA: this class = a database table
@Table(name = "users") // JPA: table will be named "users"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank
    private String password; // will be stored as bcrypt hash, never plain text

    @Enumerated(EnumType.STRING) // store role as "ADMIN" string, not 0/1/2
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean active = true; // active or inactive user

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist // runs automatically before saving to DB
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
