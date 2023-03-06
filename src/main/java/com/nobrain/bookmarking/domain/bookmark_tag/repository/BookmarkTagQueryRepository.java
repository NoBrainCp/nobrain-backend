package com.nobrain.bookmarking.domain.bookmark_tag.repository;

import com.nobrain.bookmarking.domain.bookmark_tag.dto.projection.BookmarkTagProjection;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkTagQueryRepository {

    List<BookmarkTagProjection.BookmarkAndTag> findAllBookmarkTagsByUserId(Long userId);
    List<BookmarkTagProjection.BookmarkAndTag> findBookmarkTagsByUserIdAndTagList(Long userId, List<Long> tagIds);
}
