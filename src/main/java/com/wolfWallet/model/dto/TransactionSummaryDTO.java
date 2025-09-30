package com.wolfWallet.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal balance;
    private Long transactionCount;
}