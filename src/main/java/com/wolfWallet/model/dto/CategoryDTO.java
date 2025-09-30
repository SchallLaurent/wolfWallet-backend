package com.wolfWallet.model.dto;

import com.wolfWallet.model.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private TransactionType type;
    private String color;
    private String icon;
    private Boolean isDefault;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}