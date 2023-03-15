package com.nobrain.bookmarking.domain.category.repository;

import com.nobrain.bookmarking.domain.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryQueryRepository {

    List<CategoryResponse.Info> findAllCategoryInfoWithCount(String username);
    String findCategoryNameByBookmarkId(Long bookmarkId);
}
