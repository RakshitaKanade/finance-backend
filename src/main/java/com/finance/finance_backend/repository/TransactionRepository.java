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

    // filter by type (INCOME / EXPENSE)
    List<Transaction> findByType(TransactionType type);

    // filter by category
    List<Transaction> findByCategory(String category);

    // filter by date range
    List<Transaction> findByDateBetween(LocalDate start, LocalDate end);

    // filter by type and category together
    List<Transaction> findByTypeAndCategory(TransactionType type, String category);

    // filter by who created it
    List<Transaction> findByCreatedById(Long userId);

    // Dashboard: total income
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'INCOME'")
    BigDecimal getTotalIncome();

    // Dashboard: total expense
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'EXPENSE'")
    BigDecimal getTotalExpense();

    // Dashboard: total per category
    @Query("SELECT t.category, SUM(t.amount) FROM Transaction t GROUP BY t.category")
    List<Object[]> getTotalByCategory();

    // Dashboard: monthly totals
    @Query("SELECT MONTH(t.date), YEAR(t.date), t.type, SUM(t.amount) " +
            "FROM Transaction t GROUP BY YEAR(t.date), MONTH(t.date), t.type " +
            "ORDER BY YEAR(t.date), MONTH(t.date)")
    List<Object[]> getMonthlyTotals();

    // Dashboard: recent 10 transactions
    List<Transaction> findTop10ByOrderByCreatedAtDesc();
}
