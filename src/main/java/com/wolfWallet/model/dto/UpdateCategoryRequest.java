package com.wolfWallet.model.dto;

import com.wolfWallet.model.entity.TransactionType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequest {

    @Size(max = 50, message = "Le nom ne peut pas dépasser 50 caractères")
    private String name;

    @Size(max = 255, message = "La description ne peut pas dépasser 255 caractères")
    private String description;

    private TransactionType type;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "La couleur doit être au format hexadécimal (#RRGGBB)")
    private String color;

    @Size(max = 50, message = "Le nom de l'icône ne peut pas dépasser 50 caractères")
    private String icon;
}