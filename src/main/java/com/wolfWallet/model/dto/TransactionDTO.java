package com.wolfWallet.model.dto;

import com.wolfWallet.model.entity.TransactionCategory;
import com.wolfWallet.model.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private String currency;
    private TransactionType type;
    private TransactionCategory category;
    private String description;
    private LocalDateTime transactionDate;
    private Long userId;
    private String userEmail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}