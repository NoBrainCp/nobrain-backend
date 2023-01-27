package com.nobrain.bookmarking.domain.bookmark.service;

import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkResponse;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.bookmark.repository.BookmarkRepository;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.category.exception.CategoryNotFoundException;
import com.nobrain.bookmarking.domain.category.repository.CategoryRepository;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public List<BookmarkResponse.Info> getBookmarks(String username, String categoryName) {
        User user = userRepository.findByName(username).orElseThrow(() -> new UserNotFoundException(username));
        Category category = categoryRepository.findByUserAndName(user, categoryName).orElseThrow(() -> new CategoryNotFoundException(categoryName));

        return bookmarkRepository.findAllByCategory(category).stream()
                .map(bookmark -> BookmarkResponse.Info.builder()
                        .url(bookmark.getUrl())
                        .title(bookmark.getTitle())
                        .description(bookmark.getDescription())
                        .isPublic(bookmark.isPublic())
                        .isStar(bookmark.isStar())
                        .createdAt(bookmark.getCreatedAt().toLocalDate())
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * https::// 없을시 추가 로직 구현 예정
     */
    @Transactional
    public Bookmark createBookmark(BookmarkRequest.Create dto) {
        Category category = categoryRepository.findByName(dto.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException(dto.getCategoryName()));
        Bookmark bookmark = dto.toEntity(category);
        bookmarkRepository.save(bookmark);
        return bookmark;
    }
}
