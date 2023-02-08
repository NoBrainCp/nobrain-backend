package com.nobrain.bookmarking.domain.bookmark.repository;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByCategory(Category category);

    boolean existsByUrlAndCategory(String url, Category category);
}
