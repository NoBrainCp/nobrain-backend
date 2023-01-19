package com.nobrain.bookmarking.domain.bookmark.service;

import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.bookmark.repository.BookmarkRepository;
import com.nobrain.bookmarking.domain.category.exception.CategoryNameNotFoundException;
import com.nobrain.bookmarking.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Bookmark createBookmark(BookmarkRequest.Create dto) {
        Bookmark bookmark = dto.toEntity(
                categoryRepository.findByName(dto.getCategoryName()).orElseThrow(() -> new CategoryNameNotFoundException(dto.getCategoryName())));
        bookmarkRepository.save(bookmark);
        return bookmark;
    }
}
