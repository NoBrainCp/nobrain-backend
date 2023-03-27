package com.nobrain.bookmarking.domain.bookmark.repository;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkQueryRepository {

    List<Bookmark> findAllByUserId(Long userId, Boolean isMe);
    List<Bookmark> findAllByCategoryId(Long categoryId, Boolean isMe);
    List<Bookmark> findAllStarredBookmarksByUserId(Long userId, Boolean isMe);
    Long findStarredBookmarksCountByUserId(Long userId, Boolean isMe);
    List<Bookmark> findPrivateBookmarksByUserId(Long userId);
    Long findPrivateBookmarksCountByUserId(Long userId);
    List<Bookmark> searchBookmarksByCondition(String keyword, String condition, Long userId, Boolean isMe);
    List<Bookmark> findBookmarksByUserIdAndCategoryName(Long userId, String categoryName);
}
