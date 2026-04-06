package com.finance.finance_backend.repository;

import com.finance.finance_backend.model.Transaction;
import com.finance.finance_backend.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByType(TransactionType type);

    List<Transaction> findByCategory(String category);

    List<Transaction> findByDateBetween(LocalDate start, LocalDate end);

    List<Transaction> findByTypeAndCategory(TransactionType type, String category);

    List<Transaction> findByCreatedById(Long userId);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'INCOME'")
    BigDecimal getTotalIncome();

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'EXPENSE'")
    BigDecimal getTotalExpense();

    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t GROUP BY t.category")
    List<Object[]> getTotalByCategory();

    @Query("SELECT MONTH(t.date), YEAR(t.date), t.type, SUM(t.amount) " +
            "FROM Transaction t GROUP BY YEAR(t.date), MONTH(t.date), t.type " +
            "ORDER BY YEAR(t.date), MONTH(t.date)")
    List<Object[]> getMonthlyTotals();

    // Dashboard: recent 10 transactions
    List<Transaction> findTop10ByOrderByCreatedAtDesc();
}
