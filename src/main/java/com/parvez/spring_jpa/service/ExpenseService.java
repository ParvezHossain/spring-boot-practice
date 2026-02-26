package com.parvez.spring_jpa.service;

import com.parvez.spring_jpa.dto.CreateExpenseRequest;
import com.parvez.spring_jpa.dto.ExpenseResponse;
import com.parvez.spring_jpa.dto.UpdateExpenseRequestDTO;
import com.parvez.spring_jpa.exceptions.CategoryNotFoundException;
import com.parvez.spring_jpa.exceptions.ExpenseModificationNotAllowedException;
import com.parvez.spring_jpa.exceptions.ResourceNotFoundException;
import com.parvez.spring_jpa.model.Category;
import com.parvez.spring_jpa.model.Expense;
import com.parvez.spring_jpa.repository.CategoryRepository;
import com.parvez.spring_jpa.repository.ExpenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ExpenseResponse create(CreateExpenseRequest request) {

        /**
         * Category must exist.
         * Never trust client input.
         */
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        /**
         * Optional business rule:
         * Prevent adding expense to inactive category.
         */

        if (!category.isActive()) {
            throw new IllegalStateException("Category is not active");
        }

        Expense expense = Expense.builder()
                .amount(request.amount())
                .date(request.date())
                .description(request.description())
                .category(category)
                .build();

        expenseRepository.save(expense);

        return mapToResponse(expense);
    }

    @Transactional
    public ExpenseResponse update(UUID id, UpdateExpenseRequestDTO request) {

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        validateEditable(expense);
        expense.setAmount(request.amount());
        expense.setDate(request.date());
        expense.setDescription(request.description());
        expense.setCategory(category);

        return mapToResponse(expense);
    }

    @Transactional
    public void delete(UUID id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        validateEditable(expense);
        expenseRepository.delete(expense);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<ExpenseResponse> findAll() {
        return expenseRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public boolean isDuplicate(CreateExpenseRequest request) {
        return expenseRepository
                .existsByDateAndAmountAndCategoryIdAndDescriptionIgnoreCase(
                        request.date(),
                        request.amount(),
                        request.categoryId(),
                        request.description()
                );
    }

    private void validateEditable(Expense expense) {
        int expenseYear = expense.getDate().getYear();
        int currentYear = Year.now().getValue();

        if (expenseYear != currentYear) {
            throw new ExpenseModificationNotAllowedException("Previous year expenses cannot be modified.");
        }

    }

    private ExpenseResponse mapToResponse(Expense expense) {
        return new ExpenseResponse(
                expense.getId(),
                expense.getAmount(),
                expense.getDate(),
                expense.getCategory().getId(),
                expense.getCategory().getName(),
                expense.getDescription()
        );
    }
}
