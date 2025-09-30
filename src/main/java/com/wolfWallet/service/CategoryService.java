package com.wolfWallet.service;

import com.wolfWallet.model.dto.CategoryDTO;
import com.wolfWallet.model.dto.CreateCategoryRequest;
import com.wolfWallet.model.dto.UpdateCategoryRequest;
import com.wolfWallet.mapper.CategoryMapper;
import com.wolfWallet.model.entity.Category;
import com.wolfWallet.model.entity.TransactionType;
import com.wolfWallet.model.entity.User;
import com.wolfWallet.repository.CategoryRepository;
import com.wolfWallet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CategoryMapper categoryMapper;

    // CREATE
    @Transactional
    public CategoryDTO createCategory(CreateCategoryRequest request) {
        // Vérifier que l'user existe
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'ID : " + request.getUserId()));

        // Vérifier que la catégorie n'existe pas déjà pour cet user
        if (categoryRepository.existsByNameAndUserId(request.getName(), request.getUserId())) {
            throw new RuntimeException("Une catégorie avec ce nom existe déjà pour cet utilisateur");
        }

        Category category = categoryMapper.toEntity(request, user);
        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toDTO(savedCategory);
    }

    // READ - Toutes les catégories par défaut
    public List<CategoryDTO> getDefaultCategories() {
        List<Category> categories = categoryRepository.findByIsDefaultTrueAndActiveTrue();
        return categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // READ - Catégories personnalisées d'un user
    public List<CategoryDTO> getUserCustomCategories(Long userId) {
        List<Category> categories = categoryRepository.findByUserIdAndActiveTrue(userId);
        return categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // READ - Toutes les catégories disponibles pour un user (défaut + perso)
    public List<CategoryDTO> getAllAvailableCategories(Long userId) {
        List<Category> categories = categoryRepository.findAllAvailableForUser(userId);
        return categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // READ - Catégories par type pour un user
    public List<CategoryDTO> getCategoriesByType(Long userId, TransactionType type) {
        List<Category> categories = categoryRepository.findAllAvailableForUserByType(userId, type);
        return categories.stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // READ - Une catégorie par ID
    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID : " + id));

        return categoryMapper.toDTO(category);
    }

    // UPDATE
    @Transactional
    public CategoryDTO updateCategory(Long id, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID : " + id));

        // Ne pas permettre de modifier les catégories par défaut
        if (category.getIsDefault()) {
            throw new RuntimeException("Impossible de modifier une catégorie par défaut");
        }

        // Mettre à jour uniquement les champs non-null
        if (request.getName() != null) {
            category.setName(request.getName());
        }
        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }
        if (request.getType() != null) {
            category.setType(request.getType());
        }
        if (request.getColor() != null) {
            category.setColor(request.getColor());
        }
        if (request.getIcon() != null) {
            category.setIcon(request.getIcon());
        }

        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toDTO(updatedCategory);
    }

    // DELETE (soft delete)
    @Transactional
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID : " + id));

        // Ne pas permettre de supprimer les catégories par défaut
        if (category.getIsDefault()) {
            throw new RuntimeException("Impossible de supprimer une catégorie par défaut");
        }

        category.setActive(false);
        categoryRepository.save(category);
    }
}