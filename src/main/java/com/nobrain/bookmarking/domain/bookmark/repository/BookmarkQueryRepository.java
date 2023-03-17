package com.nobrain.bookmarking.domain.bookmark.repository;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkQueryRepository {

    List<Bookmark> findAllByUserId(Long userId);
    List<Bookmark> searchBookmarksByCondition(String keyword, String condition, Long userId);
}
