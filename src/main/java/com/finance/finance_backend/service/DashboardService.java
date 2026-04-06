package com.finance.finance_backend.service;

import com.finance.finance_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final TransactionRepository transactionRepository;

    // Full dashboard summary
    public Map<String, Object> getSummary() {
        BigDecimal totalIncome = transactionRepository.getTotalIncome();
        BigDecimal totalExpense = transactionRepository.getTotalExpense();
        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("totalIncome", totalIncome);
        summary.put("totalExpense", totalExpense);
        summary.put("netBalance", netBalance);
        summary.put("categoryTotals", getCategoryTotals());
        summary.put("monthlyTrends", getMonthlyTrends());
        summary.put("recentActivity", getRecentActivity());

        return summary;
    }

    // Category wise totals
    public Map<String, BigDecimal> getCategoryTotals() {
        List<Object[]> results = transactionRepository.getTotalByCategory();
        Map<String, BigDecimal> categoryMap = new LinkedHashMap<>();
        for (Object[] row : results) {
            categoryMap.put((String) row[0], (BigDecimal) row[1]);
        }
        return categoryMap;
    }

    // Monthly trends
    public List<Map<String, Object>> getMonthlyTrends() {
        List<Object[]> results = transactionRepository.getMonthlyTotals();
        return results.stream().map(row -> {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("month", row[0]);
            entry.put("year", row[1]);
            entry.put("type", row[2]);
            entry.put("total", row[3]);
            return entry;
        }).toList();
    }

    // Recent 10 transactions
    public List<Map<String, Object>> getRecentActivity() {
        return transactionRepository.findTop10ByOrderByCreatedAtDesc()
                .stream().map(t -> {
                    Map<String, Object> entry = new LinkedHashMap<>();
                    entry.put("id", t.getId());
                    entry.put("amount", t.getAmount());
                    entry.put("type", t.getType());
                    entry.put("category", t.getCategory());
                    entry.put("date", t.getDate());
                    return entry;
                }).toList();
    }
}
