package com.nobrain.bookmarking.domain.category.service;

import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
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

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryQueryRepository categoryQueryRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryResponse.Header getCategoryHeader(String username, String categoryName) {
        User user = findUserByUsername(username);
        Category category = categoryRepository.findByUserAndName(user, categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));

        return CategoryResponse.Header.toDto(category);
    }

    public List<CategoryResponse.Info> getCategories(UserPayload myPayload, String username) {
        Long userId = findUserByUsername(username).getId();
        boolean isMe = isMe(myPayload.getUserId(), userId);
        return categoryQueryRepository.findAllCategoryInfoWithCount(username, isMe);
    }

    public CategoryResponse.Info getCategoryByBookmarkId(Long bookmarkId) {
        return categoryQueryRepository.findCategoryByBookmarkId(bookmarkId);
    }

    public Boolean getCategoryIdPublic(Long userId, String categoryName) {
        return categoryQueryRepository.findCategoryIsPublic(userId, categoryName);
    }

    @Transactional
    public String create(UserPayload payload, CategoryRequest.Info categoryInfo) {
        User user = findUserByUsername(payload.getUsername());
        validateCategoryDuplication(user, categoryInfo.getName());
        return categoryRepository.save(categoryInfo.toEntity(user)).getName();
    }

    @Transactional
    public void updateCategory(UserPayload payload, String originCategoryName, CategoryRequest.Info categoryInfo) {
        User user = findUserByUsername(payload.getUsername());
        Category category = categoryRepository.findByUserAndName(user, originCategoryName)
                .orElseThrow(() -> new CategoryNotFoundException(originCategoryName));

        validateUpdateCategoryDuplication(user, originCategoryName, categoryInfo.getName());
        category.update(categoryInfo);
    }

    @Transactional
    public void deleteCategory(UserPayload payload, String categoryName) {
        User user = findUserByUsername(payload.getUsername());
        categoryRepository.deleteByUserAndName(user, categoryName);
    }

    private void validateCategoryDuplication(User user, String categoryName) {
        if (categoryRepository.existsByUserAndName(user, categoryName)) {
            throw new CategoryNameDuplicationException(categoryName);
        }
    }

    private void validateUpdateCategoryDuplication(User user, String originName, String changeName) {
        if (!originName.equals(changeName) && categoryRepository.existsByUserAndName(user, changeName)) {
            throw new CategoryNameDuplicationException(changeName);
        }
    }

    private User findUserByUsername(String username) {
        return userRepository.findByName(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    private boolean isMe(Long myId, Long userId) {
        return Objects.equals(myId, userId);
    }
}
