package com.wolfWallet.mapper;

import com.wolfWallet.model.dto.CategoryDTO;
import com.wolfWallet.model.dto.CreateCategoryRequest;
import com.wolfWallet.model.entity.Category;
import com.wolfWallet.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setType(category.getType());
        dto.setColor(category.getColor());
        dto.setIcon(category.getIcon());
        dto.setIsDefault(category.getIsDefault());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());

        if (category.getUser() != null) {
            dto.setUserId(category.getUser().getId());
        }

        return dto;
    }

    public Category toEntity(CreateCategoryRequest request, User user) {
        if (request == null) {
            return null;
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setType(request.getType());
        category.setColor(request.getColor());
        category.setIcon(request.getIcon());
        category.setIsDefault(false); // Toujours false pour les catégories créées par user
        category.setUser(user);
        category.setActive(true);

        return category;
    }
}