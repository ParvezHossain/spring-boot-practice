package com.parvez.spring_jpa.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateCategoryDTO(
        @NotNull(message = "Active status must be provided!")
        Boolean active
) {
}
