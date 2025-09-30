package com.wolfWallet.model.dto;

import com.wolfWallet.model.entity.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionRequest {

    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
    @Digits(integer = 13, fraction = 2, message = "Format du montant invalide")
    private BigDecimal amount;

    @Size(min = 3, max = 3, message = "La devise doit faire 3 caractères")
    private String currency;

    private TransactionType type;

    // ← MODIFIÉ : categoryId au lieu de category enum
    private Long categoryId;

    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
    private String description;

    private LocalDateTime transactionDate;
}