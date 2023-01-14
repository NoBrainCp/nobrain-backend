package com.nobrain.bookmarking.domain.category.repository;

import com.nobrain.bookmarking.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);
}
