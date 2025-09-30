package com.wolfWallet.repository;

import com.wolfWallet.model.entity.Category;
import com.wolfWallet.model.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Catégories par défaut (système)
    List<Category> findByIsDefaultTrueAndActiveTrue();

    // Catégories par défaut d'un type spécifique
    List<Category> findByIsDefaultTrueAndTypeAndActiveTrue(TransactionType type);

    // Catégories personnalisées d'un user
    List<Category> findByUserIdAndActiveTrue(Long userId);

    // Catégories personnalisées d'un user par type
    List<Category> findByUserIdAndTypeAndActiveTrue(Long userId, TransactionType type);

    // Toutes les catégories disponibles pour un user (défaut + perso)
    @Query("SELECT c FROM Category c WHERE (c.isDefault = true OR c.user.id = :userId) AND c.active = true")
    List<Category> findAllAvailableForUser(@Param("userId") Long userId);

    // Toutes les catégories d'un type pour un user
    @Query("SELECT c FROM Category c WHERE (c.isDefault = true OR c.user.id = :userId) AND c.type = :type AND c.active = true")
    List<Category> findAllAvailableForUserByType(@Param("userId") Long userId, @Param("type") TransactionType type);

    // Vérifier si une catégorie avec ce nom existe déjà pour un user
    boolean existsByNameAndUserId(String name, Long userId);

    // Trouver une catégorie par nom (pour un user)
    Optional<Category> findByNameAndUserId(String name, Long userId);
}