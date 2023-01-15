package com.nobrain.bookmarking.domain.category.service;

import com.nobrain.bookmarking.domain.category.dto.request.AddCategoryRequest;
import com.nobrain.bookmarking.domain.category.exception.CategoryNameDuplicationException;
import com.nobrain.bookmarking.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public void addCategory(AddCategoryRequest dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new CategoryNameDuplicationException(dto.getName());
        }

        categoryRepository.save(dto.toEntity());
    }
}
