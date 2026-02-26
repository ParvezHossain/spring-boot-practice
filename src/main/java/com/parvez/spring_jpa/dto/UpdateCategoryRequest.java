package com.parvez.spring_jpa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCategoryRequest(
        @NotBlank(message = "Category can not be empty")
        @Size(max = 50, message = "Category must not exceed 50 characters")
        String name
) {
}
