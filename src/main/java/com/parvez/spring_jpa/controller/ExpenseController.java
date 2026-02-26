package com.parvez.spring_jpa.controller;

import com.parvez.spring_jpa.config.ApiPaths;
import com.parvez.spring_jpa.dto.CreateExpenseRequest;
import com.parvez.spring_jpa.dto.ExpenseResponse;
import com.parvez.spring_jpa.dto.UpdateExpenseRequestDTO;
import com.parvez.spring_jpa.exceptions.DuplicateExpenseException;
import com.parvez.spring_jpa.model.Expense;
import com.parvez.spring_jpa.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.EXPENSES)
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','HR')")
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse create(
            @Valid @RequestBody CreateExpenseRequest request
    ) {

        boolean duplicateExists = expenseService.isDuplicate(request);

        if (duplicateExists && !request.forceSave()) {
            throw new DuplicateExpenseException(
                    "A similar expense already exists."
            );
        }
        return expenseService.create(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ExpenseResponse> findAll() {
        return expenseService.findAll();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ExpenseResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateExpenseRequestDTO request
    ) {
        ExpenseResponse expense = expenseService.update(id, request);
        return expense;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        expenseService.delete(id);
    }
}
