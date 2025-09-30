package com.wolfWallet.controller;

import com.wolfWallet.model.dto.*;
import com.wolfWallet.model.entity.TransactionType;
import com.wolfWallet.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // CREATE - Créer une nouvelle transaction
    @PostMapping
    public ResponseEntity<TransactionDTO> createTransaction(@Valid @RequestBody CreateTransactionRequest request) {
        TransactionDTO createdTransaction = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }

    // READ - Récupérer toutes les transactions d'un utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDTO>> getUserTransactions(@PathVariable Long userId) {
        List<TransactionDTO> transactions = transactionService.getAllUserTransactions(userId);
        return ResponseEntity.ok(transactions);
    }

    // READ - Récupérer une transaction par ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        TransactionDTO transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    // READ - Récupérer les transactions par type (INCOME ou EXPENSE)
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByType(
            @PathVariable Long userId,
            @PathVariable TransactionType type) {
        List<TransactionDTO> transactions = transactionService.getTransactionsByType(userId, type);
        return ResponseEntity.ok(transactions);
    }

    // READ - Récupérer les transactions entre deux dates
    @GetMapping("/user/{userId}/period")
    public ResponseEntity<List<TransactionDTO>> getTransactionsBetweenDates(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<TransactionDTO> transactions = transactionService.getTransactionsBetweenDates(userId, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    // READ - Récupérer le résumé des transactions (revenus, dépenses, balance)
    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<TransactionSummaryDTO> getTransactionSummary(@PathVariable Long userId) {
        TransactionSummaryDTO summary = transactionService.getTransactionSummary(userId);
        return ResponseEntity.ok(summary);
    }

    // UPDATE - Modifier une transaction
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTransactionRequest request) {
        TransactionDTO updatedTransaction = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(updatedTransaction);
    }

    // DELETE - Supprimer une transaction (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    // DELETE - Supprimer définitivement une transaction (hard delete)
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> permanentDeleteTransaction(@PathVariable Long id) {
        transactionService.permanentDeleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}