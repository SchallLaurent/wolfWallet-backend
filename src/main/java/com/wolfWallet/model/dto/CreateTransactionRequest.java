package com.wolfWallet.model.dto;

import com.wolfWallet.model.entity.TransactionType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionRequest {

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être supérieur à 0")
    @Digits(integer = 13, fraction = 2, message = "Format du montant invalide")
    private BigDecimal amount;

    @NotBlank(message = "La devise est obligatoire")
    @Size(min = 3, max = 3, message = "La devise doit faire 3 caractères (ex: EUR, USD)")
    private String currency;

    @NotNull(message = "Le type de transaction est obligatoire")
    private TransactionType type;

    // ← MODIFIÉ : categoryId au lieu de category enum
    @NotNull(message = "La catégorie est obligatoire")
    private Long categoryId;

    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
    private String description;

    @NotNull(message = "La date de transaction est obligatoire")
    private LocalDateTime transactionDate;

    @NotNull(message = "L'ID de l'utilisateur est obligatoire")
    private Long userId;
}