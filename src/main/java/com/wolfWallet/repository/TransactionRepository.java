package com.wolfWallet.repository;

import com.wolfWallet.model.entity.Transaction;
import com.wolfWallet.model.entity.TransactionCategory;
import com.wolfWallet.model.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Trouver toutes les transactions d'un user
    List<Transaction> findByUserId(Long userId);

    // Trouver les transactions par type (INCOME ou EXPENSE)
    List<Transaction> findByUserIdAndType(Long userId, TransactionType type);

    // Trouver les transactions entre deux dates
    List<Transaction> findByUserIdAndTransactionDateBetween(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Trouver les transactions par catégorie
    List<Transaction> findByUserIdAndCategory(Long userId, TransactionCategory category);

    // Calculer le total des revenus d'un user
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.type = 'INCOME' AND t.active = true")
    BigDecimal calculateTotalIncome(@Param("userId") Long userId);

    // Calculer le total des dépenses d'un user
    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user.id = :userId AND t.type = 'EXPENSE' AND t.active = true")
    BigDecimal calculateTotalExpenses(@Param("userId") Long userId);

    // Transactions actives d'un user
    List<Transaction> findByUserIdAndActiveTrue(Long userId);

    // Compter les transactions actives d'un user
    Long countByUserIdAndActiveTrue(Long userId);
}