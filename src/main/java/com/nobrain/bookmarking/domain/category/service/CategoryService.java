package com.nobrain.bookmarking.domain.category.service;

import com.nobrain.bookmarking.domain.category.dto.CategoryRequest;
import com.nobrain.bookmarking.domain.category.dto.CategoryResponse;
import com.nobrain.bookmarking.domain.category.exception.CategoryNameDuplicationException;
import com.nobrain.bookmarking.domain.category.repository.CategoryRepository;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<CategoryResponse.Info> getCategories(String username) {
        User user = userRepository.findByName(username).orElseThrow(() -> new UserNotFoundException(username));
        return categoryRepository.findAllByUser(user).stream()
                .map(category -> CategoryResponse.Info.builder()
                .name(category.getName())
                .description(category.getDescription())
                .isPublic(category.isPublic())
                .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public String create(String username, CategoryRequest.Create dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new CategoryNameDuplicationException(dto.getName());
        }

        User user = userRepository.findByName(username).orElseThrow(() -> new UserNotFoundException(username));
        return categoryRepository.save(dto.toEntity(user)).getName();
    }
}
