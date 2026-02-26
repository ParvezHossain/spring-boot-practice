package com.parvez.spring_jpa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryRequest(
        @NotBlank(message = "Category name can not be empty")
        @Size(max = 50, message = "Category name must not exceed 50 characters")
        String name
) {
}
