package com.nobrain.bookmarking.domain.bookmark_tag.repository;

import com.nobrain.bookmarking.domain.bookmark_tag.entity.BookmarkTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkTagRepository extends JpaRepository<BookmarkTag, Long> {
}
