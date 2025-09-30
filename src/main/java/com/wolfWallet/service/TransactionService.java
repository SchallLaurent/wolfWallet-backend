package com.wolfWallet.service;

import com.wolfWallet.model.dto.*;
import com.wolfWallet.mapper.TransactionMapper;
import com.wolfWallet.model.entity.Transaction;
import com.wolfWallet.model.entity.TransactionType;
import com.wolfWallet.model.entity.User;
import com.wolfWallet.repository.TransactionRepository;
import com.wolfWallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;


    // CREATE
    @Transactional
    public TransactionDTO createTransaction(CreateTransactionRequest request) {
        // Vérifier que l'utilisateur existe
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + request.getUserId()));

        // Créer la transaction
        Transaction transaction = transactionMapper.toEntity(request, user);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return transactionMapper.toDTO(savedTransaction);
    }

    // READ - Toutes les transactions d'un user
    public List<TransactionDTO> getAllUserTransactions(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndActiveTrue(userId);
        return transactions.stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // READ - Une transaction par ID
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction non trouvée avec l'ID : " + id));

        return transactionMapper.toDTO(transaction);
    }

    // READ - Transactions par type (INCOME ou EXPENSE)
    public List<TransactionDTO> getTransactionsByType(Long userId, TransactionType type) {
        List<Transaction> transactions = transactionRepository.findByUserIdAndType(userId, type);
        return transactions.stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // READ - Transactions entre deux dates
    public List<TransactionDTO> getTransactionsBetweenDates(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository
                .findByUserIdAndTransactionDateBetween(userId, startDate, endDate);
        return transactions.stream()
                .map(transactionMapper::toDTO)
                .collect(Collectors.toList());
    }

    // READ - Résumé des transactions (revenus, dépenses, balance)
    public TransactionSummaryDTO getTransactionSummary(Long userId) {
        BigDecimal totalIncome = transactionRepository.calculateTotalIncome(userId);
        BigDecimal totalExpenses = transactionRepository.calculateTotalExpenses(userId);

        // Gérer les null (si pas de transactions)
        totalIncome = totalIncome != null ? totalIncome : BigDecimal.ZERO;
        totalExpenses = totalExpenses != null ? totalExpenses : BigDecimal.ZERO;

        BigDecimal balance = totalIncome.subtract(totalExpenses);
        Long count = transactionRepository.countByUserIdAndActiveTrue(userId);

        return new TransactionSummaryDTO(totalIncome, totalExpenses, balance, count);
    }

    // UPDATE
    @Transactional
    public TransactionDTO updateTransaction(Long id, UpdateTransactionRequest request) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction non trouvée avec l'ID : " + id));

        // Mettre à jour uniquement les champs non-null
        if (request.getAmount() != null) {
            transaction.setAmount(request.getAmount());
        }
        if (request.getCurrency() != null) {
            transaction.setCurrency(request.getCurrency());
        }
        if (request.getType() != null) {
            transaction.setType(request.getType());
        }
        if (request.getCategory() != null) {
            transaction.setCategory(request.getCategory());
        }
        if (request.getDescription() != null) {
            transaction.setDescription(request.getDescription());
        }
        if (request.getTransactionDate() != null) {
            transaction.setTransactionDate(request.getTransactionDate());
        }

        Transaction updatedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDTO(updatedTransaction);
    }

    // DELETE (soft delete - met active à false)
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction non trouvée avec l'ID : " + id));

        transaction.setActive(false);
        transactionRepository.save(transaction);
    }

    // DELETE (hard delete - suppression définitive)
    @Transactional
    public void permanentDeleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new RuntimeException("Transaction non trouvée avec l'ID : " + id);
        }
        transactionRepository.deleteById(id);
    }
}