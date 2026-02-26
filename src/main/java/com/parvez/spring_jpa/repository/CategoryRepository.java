package com.parvez.spring_jpa.repository;

import com.parvez.spring_jpa.model.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByNameIgnoreCase(String name);
    List<Category> findAllByActiveTrue(Sort sort);
    List<Category> findAllByActiveFalse();
}
