package com.finance.finance_backend.config;

import com.finance.finance_backend.model.Role;
import com.finance.finance_backend.model.Transaction;
import com.finance.finance_backend.model.TransactionType;
import com.finance.finance_backend.model.User;
import com.finance.finance_backend.repository.TransactionRepository;
import com.finance.finance_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Only seed if DB is empty
        if (userRepository.count() > 0) return;

        // Create admin user
        User admin = userRepository.save(User.builder()
                .name("Admin User")
                .email("admin@finance.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .active(true)
                .build());

        // Create analyst user
        User analyst = userRepository.save(User.builder()
                .name("Analyst User")
                .email("analyst@finance.com")
                .password(passwordEncoder.encode("analyst123"))
                .role(Role.ANALYST)
                .active(true)
                .build());

        // Create viewer user
        userRepository.save(User.builder()
                .name("Viewer User")
                .email("viewer@finance.com")
                .password(passwordEncoder.encode("viewer123"))
                .role(Role.VIEWER)
                .active(true)
                .build());

        // Seed some sample transactions
        transactionRepository.save(Transaction.builder()
                .amount(new BigDecimal("50000.00"))
                .type(TransactionType.INCOME)
                .category("Salary")
                .date(LocalDate.now().minusDays(5))
                .notes("Monthly salary")
                .createdBy(admin)
                .build());

        transactionRepository.save(Transaction.builder()
                .amount(new BigDecimal("1200.00"))
                .type(TransactionType.EXPENSE)
                .category("Food")
                .date(LocalDate.now().minusDays(3))
                .notes("Grocery shopping")
                .createdBy(analyst)
                .build());

        transactionRepository.save(Transaction.builder()
                .amount(new BigDecimal("15000.00"))
                .type(TransactionType.EXPENSE)
                .category("Rent")
                .date(LocalDate.now().minusDays(1))
                .notes("Monthly rent")
                .createdBy(admin)
                .build());

        transactionRepository.save(Transaction.builder()
                .amount(new BigDecimal("5000.00"))
                .type(TransactionType.INCOME)
                .category("Freelance")
                .date(LocalDate.now())
                .notes("Freelance project payment")
                .createdBy(analyst)
                .build());

        System.out.println("✅ Sample data seeded successfully!");
        System.out.println("👤 Admin:    admin@finance.com   / admin123");
        System.out.println("👤 Analyst:  analyst@finance.com / analyst123");
        System.out.println("👤 Viewer:   viewer@finance.com  / viewer123");
    }
}
