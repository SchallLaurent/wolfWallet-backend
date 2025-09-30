package com.wolfWallet.mapper;

import com.wolfWallet.model.dto.CreateTransactionRequest;
import com.wolfWallet.model.dto.TransactionDTO;
import com.wolfWallet.model.entity.Transaction;
import com.wolfWallet.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    // Entity → DTO
    public TransactionDTO toDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionDTO dto = new TransactionDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setCurrency(transaction.getCurrency());
        dto.setType(transaction.getType());
        dto.setCategory(transaction.getCategory());
        dto.setDescription(transaction.getDescription());
        dto.setTransactionDate(transaction.getTransactionDate());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());

        // Informations du user
        if (transaction.getUser() != null) {
            dto.setUserId(transaction.getUser().getId());
            dto.setUserEmail(transaction.getUser().getEmail());
        }

        return dto;
    }

    // CreateRequest → Entity
    public Transaction toEntity(CreateTransactionRequest request, User user) {
        if (request == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setCurrency(request.getCurrency());
        transaction.setType(request.getType());
        transaction.setCategory(request.getCategory());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setUser(user);
        transaction.setActive(true);

        return transaction;
    }
}