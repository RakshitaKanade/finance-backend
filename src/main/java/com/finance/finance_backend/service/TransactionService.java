package com.finance.finance_backend.service;

import com.finance.finance_backend.dto.TransactionRequest;
import com.finance.finance_backend.dto.TransactionResponse;
import com.finance.finance_backend.exception.ApiException;
import com.finance.finance_backend.model.Transaction;
import com.finance.finance_backend.model.TransactionType;
import com.finance.finance_backend.model.User;
import com.finance.finance_backend.repository.TransactionRepository;
import com.finance.finance_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    // Get all transactions with optional filters
    public List<TransactionResponse> getAll(
            TransactionType type, String category,
            LocalDate startDate, LocalDate endDate) {

        // Apply filters based on what's provided
        if (type != null && category != null) {
            return toResponseList(
                    transactionRepository.findByTypeAndCategory(type, category));
        } else if (type != null) {
            return toResponseList(transactionRepository.findByType(type));
        } else if (category != null) {
            return toResponseList(transactionRepository.findByCategory(category));
        } else if (startDate != null && endDate != null) {
            return toResponseList(
                    transactionRepository.findByDateBetween(startDate, endDate));
        }

        return toResponseList(transactionRepository.findAll());
    }

    // Get single transaction
    public TransactionResponse getById(Long id) {
        return toResponse(findById(id));
    }

    // Create transaction
    public TransactionResponse create(TransactionRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(
                        "User not found", HttpStatus.NOT_FOUND));

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate())
                .notes(request.getNotes())
                .createdBy(user)
                .build();

        return toResponse(transactionRepository.save(transaction));
    }

    // Update transaction
    public TransactionResponse update(Long id, TransactionRequest request) {
        Transaction transaction = findById(id);

        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setDate(request.getDate());
        transaction.setNotes(request.getNotes());

        return toResponse(transactionRepository.save(transaction));
    }

    // Delete transaction
    public void delete(Long id) {
        Transaction transaction = findById(id);
        transactionRepository.delete(transaction);
    }

    // Helper: find or throw 404
    private Transaction findById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ApiException(
                        "Transaction not found", HttpStatus.NOT_FOUND));
    }

    // Helper: convert to response DTO
    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getAmount(),
                t.getType(),
                t.getCategory(),
                t.getDate(),
                t.getNotes(),
                t.getCreatedBy().getEmail(),
                t.getCreatedAt()
        );
    }

    // Helper: convert list
    private List<TransactionResponse> toResponseList(List<Transaction> list) {
        return list.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
