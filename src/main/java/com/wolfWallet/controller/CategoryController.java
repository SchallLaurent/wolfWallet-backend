package com.wolfWallet.controller;

import com.wolfWallet.model.dto.CategoryDTO;
import com.wolfWallet.model.dto.CreateCategoryRequest;
import com.wolfWallet.model.dto.UpdateCategoryRequest;
import com.wolfWallet.model.entity.TransactionType;
import com.wolfWallet.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    // CREATE - Créer une catégorie personnalisée
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        CategoryDTO createdCategory = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCategory);
    }

    // READ - Récupérer toutes les catégories par défaut
    @GetMapping("/default")
    public ResponseEntity<List<CategoryDTO>> getDefaultCategories() {
        List<CategoryDTO> categories = categoryService.getDefaultCategories();
        return ResponseEntity.ok(categories);
    }

    // READ - Récupérer les catégories personnalisées d'un user
    @GetMapping("/user/{userId}/custom")
    public ResponseEntity<List<CategoryDTO>> getUserCustomCategories(@PathVariable Long userId) {
        List<CategoryDTO> categories = categoryService.getUserCustomCategories(userId);
        return ResponseEntity.ok(categories);
    }

    // READ - Récupérer toutes les catégories disponibles pour un user (défaut + perso)
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CategoryDTO>> getAllAvailableCategories(@PathVariable Long userId) {
        List<CategoryDTO> categories = categoryService.getAllAvailableCategories(userId);
        return ResponseEntity.ok(categories);
    }

    // READ - Récupérer les catégories par type pour un user
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<CategoryDTO>> getCategoriesByType(
            @PathVariable Long userId,
            @PathVariable TransactionType type) {
        List<CategoryDTO> categories = categoryService.getCategoriesByType(userId, type);
        return ResponseEntity.ok(categories);
    }

    // READ - Récupérer une catégorie par ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        CategoryDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    // UPDATE - Modifier une catégorie personnalisée
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(updatedCategory);
    }

    // DELETE - Supprimer une catégorie personnalisée (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}