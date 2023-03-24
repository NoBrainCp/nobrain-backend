package com.nobrain.bookmarking.domain.category.repository;

import com.nobrain.bookmarking.domain.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryQueryRepository {

    List<CategoryResponse.Info> findAllCategoryInfoWithCount(String username);
    CategoryResponse.Info findCategoryByBookmarkId(Long bookmarkId);
    Boolean findCategoryIsPublic(Long userId, String categoryName);
}
