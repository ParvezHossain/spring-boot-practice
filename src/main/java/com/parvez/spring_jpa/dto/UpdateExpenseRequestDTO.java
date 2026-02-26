package com.parvez.spring_jpa.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record UpdateExpenseRequestDTO(
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @NotNull(message = "Date is required")
        @PastOrPresent(message = "Date can not be future")
        LocalDate date,

        @NotNull(message = "Category is required")
        UUID categoryId,

        @NotNull(message = "Description can not be empty")
        String description,

        Boolean forceSave
) {
    /**
     * Compact constructor
     * Used to normalize input values
     */
    public UpdateExpenseRequestDTO {
        if (description != null) {
            description = description.trim();
            if (description.isEmpty()) {
                description = null;
            }
        }

        // default forceSave to false if null
        if (forceSave == null) {
            forceSave = false;
        }
    }
}


//
//BigDecimal amount,
//LocalDate date,
//UUID categoryId,
//String categoryName,
//String description