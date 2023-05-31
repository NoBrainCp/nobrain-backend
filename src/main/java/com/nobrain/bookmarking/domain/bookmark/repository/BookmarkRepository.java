package com.nobrain.bookmarking.domain.bookmark.repository;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUrlAndCategory(String url, Category category);
}
