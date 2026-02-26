package com.parvez.spring_jpa.repository;

import com.parvez.spring_jpa.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    boolean existsByDateAndAmountAndCategoryIdAndDescriptionIgnoreCase(
            LocalDate date,
            BigDecimal amount,
            UUID categoryId,
            String description
    );
}
