package com.finance.finance_backend.repository;

import com.finance.finance_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring auto-generates the SQL for these just from the method name!
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

