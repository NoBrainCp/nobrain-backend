package com.nobrain.bookmarking.domain.category.service;

import com.nobrain.bookmarking.domain.auth.service.TokenService;
import com.nobrain.bookmarking.domain.category.dto.CategoryRequest;
import com.nobrain.bookmarking.domain.category.dto.CategoryResponse;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.category.exception.CategoryNameDuplicationException;
import com.nobrain.bookmarking.domain.category.exception.CategoryNotFoundException;
import com.nobrain.bookmarking.domain.category.repository.CategoryQueryRepository;
import com.nobrain.bookmarking.domain.category.repository.CategoryRepository;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryQueryRepository categoryQueryRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public CategoryResponse.Info getCategory(String username, String categoryName) {
        User user = findUserByUsername(username);
        Category category = categoryRepository.findByUserAndName(user, categoryName).orElseThrow(() -> new CategoryNotFoundException(categoryName));
        return CategoryResponse.Info.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .isPublic(category.isPublic())
                .build();
    }

    public List<CategoryResponse.Info> getCategories(String username) {
        Long userId = findUserByUsername(username).getId();
        boolean isMe = isMe(userId);
        return categoryQueryRepository.findAllCategoryInfoWithCount(username, isMe).stream()
                .map(category -> CategoryResponse.Info.builder()
                    .id(category.getId())
                    .name(category.getName())
                    .description(category.getDescription())
                    .isPublic(category.isPublic())
                    .count(category.getCount())
                    .build())
                .collect(Collectors.toList());
    }

    public CategoryResponse.Info getCategoryByBookmarkId(Long bookmarkId) {
        return categoryQueryRepository.findCategoryByBookmarkId(bookmarkId);
    }

    public Boolean getCategoryIdPublic(Long userId, String categoryName) {
        return categoryQueryRepository.findCategoryIsPublic(userId, categoryName);
    }

    @Transactional
    public String create(String username, CategoryRequest.Info requestDto) {
        User user = findUserByUsername(username);
        if (categoryRepository.existsByUserAndName(user, requestDto.getName())) {
            throw new CategoryNameDuplicationException(requestDto.getName());
        }

        return categoryRepository.save(requestDto.toEntity(user)).getName();
    }

    @Transactional
    public void updateCategory(String username, String categoryName, CategoryRequest.Info requestDto) {
        User user = findUserByUsername(username);
        Category category = categoryRepository.findByUserAndName(user, categoryName).orElseThrow(() -> new CategoryNotFoundException(categoryName));

        if (!categoryName.equals(requestDto.getName()) && categoryRepository.existsByUserAndName(user, requestDto.getName())) {
            throw new CategoryNameDuplicationException(requestDto.getName());
        }

        category.update(requestDto);
    }

    @Transactional
    public void deleteCategory(String username, String categoryName) {
        User user = findUserByUsername(username);
        categoryRepository.deleteByUserAndName(user, categoryName);
    }

    private Category findCategoryByCategoryId(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new CategoryNotFoundException(String.valueOf(categoryId)));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByName(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    private boolean isMe(Long userId) {
        Long myId = tokenService.getId();
        return Objects.equals(userId, myId);
    }
}
