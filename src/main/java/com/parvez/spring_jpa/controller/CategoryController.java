package com.parvez.spring_jpa.controller;

import com.parvez.spring_jpa.config.ApiPaths;
import com.parvez.spring_jpa.dto.CategoryResponse;
import com.parvez.spring_jpa.dto.CreateCategoryRequest;
import com.parvez.spring_jpa.dto.UpdateCategoryDTO;
import com.parvez.spring_jpa.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.CATEGORIES)
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN','HR')")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Create a new Category.
     * - POST returns 201 Created
     * - Response body contains the created resource
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(
            @Valid @RequestBody CreateCategoryRequest request
    ) {
        return categoryService.create(request);
    }

    /**
     * Update category active status.
     * - PUT is idempotent (same request = same result)
     * - Return updated resource
     * - Validation handled via @Valid
     */
    @PutMapping("/{id}")
    public CategoryResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCategoryDTO request
    ) {
        return categoryService.update(id, request);
    }

    /**
     * Soft delete category.
     * - DELETE should return 204 No Content
     * - No body returned
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id
    ) {
        categoryService.delete(id);
    }

    /**
     * Retrieve categories.
     * - GET returns 200 OK
     * - Filtering via request param
     * - Default activeOnly = true
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> findAll(
            @RequestParam(defaultValue = "true") boolean activeOnly
    ) {
        return categoryService.findAll(activeOnly);
    }
}
