package com.parvez.spring_jpa.service;

import com.parvez.spring_jpa.dto.CategoryResponse;
import com.parvez.spring_jpa.dto.CreateCategoryRequest;
import com.parvez.spring_jpa.dto.UpdateCategoryDTO;
import com.parvez.spring_jpa.dto.UpdateCategoryRequest;
import com.parvez.spring_jpa.exceptions.CategoryNotFoundException;
import com.parvez.spring_jpa.exceptions.DuplicateCategoryException;
import com.parvez.spring_jpa.model.Category;
import com.parvez.spring_jpa.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

/*
    @Transactional(
            propagation = Propagation.REQUIRED,
            isolation = Isolation.DEFAULT,
            readOnly = false,
            rollbackFor = RuntimeException.class
    )
*/

    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        String normalizedName = request.name().trim();

        if (categoryRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new DuplicateCategoryException("Category name already exists");
        }

        Category category = Category
                .builder()
                .name(normalizedName)
                .active(true)
                .build();

        Category savedCategory = categoryRepository.save(category);
        return mapToResponse(savedCategory);
    }
    @Transactional
    public CategoryResponse update(UUID id, UpdateCategoryDTO request) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category not found")
                );

        category.setActive(request.active());

        System.out.println(category);

        return mapToResponse(category);
    }

    @Transactional
    public void delete(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
        category.setActive(false);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll(boolean activeOnly) {

        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        List<Category> categories = activeOnly
                ? categoryRepository.findAllByActiveTrue(sort)
                : categoryRepository.findAll(sort);

        return categories.stream()
                .map(this::mapToResponse)
                .toList();
    }


    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.isActive()
        );
    }
}
