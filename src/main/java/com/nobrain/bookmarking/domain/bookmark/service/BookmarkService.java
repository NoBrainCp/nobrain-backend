package com.nobrain.bookmarking.domain.bookmark.service;

import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkResponse;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.bookmark.exception.BookmarkDuplicationException;
import com.nobrain.bookmarking.domain.bookmark.exception.BookmarkNotFoundException;
import com.nobrain.bookmarking.domain.bookmark.repository.BookmarkQueryRepository;
import com.nobrain.bookmarking.domain.bookmark.repository.BookmarkRepository;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark_tag.service.BookmarkTagService;
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

    private final BookmarkQueryRepository bookmarkQueryRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BookmarkTagService bookmarkTagService;

    public List<BookmarkResponse.Info> getAllBookmarksByUsername(String username) {
        Long userId = userRepository.findByName(username).orElseThrow(() -> new UserNotFoundException(username)).getId();
        return bookmarkQueryRepository.findAllByUser(userId).stream()
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


    public List<BookmarkResponse.Info> getBookmarksByCategory(String username, String categoryName) {
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

    @Transactional
    public void createBookmark(String username, BookmarkRequest.Info requestDto) {
        String url = requestDto.getUrl();
        if (!url.contains("https://")) {
            requestDto.setUrl(url);
        }

        User user = userRepository.findByName(username).orElseThrow(() -> new UserNotFoundException(username));
        Category category = categoryRepository.findByUserAndName(user, requestDto.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException(requestDto.getCategoryName()));
        Bookmark bookmark = requestDto.toEntity(category);

        validateBookmark(requestDto, category);
        bookmarkRepository.save(bookmark);

        bookmarkTagService.saveTags(bookmark, requestDto.getTags());
    }

    @Transactional
    public void updateBookmark(long bookmarkId, BookmarkRequest.Info requestDto) {
        Bookmark bookmark = findById(bookmarkId);
        Category category = categoryRepository.findByName(requestDto.getCategoryName())
                .orElseThrow(() -> new CategoryNotFoundException(requestDto.getCategoryName()));

        bookmark.update(requestDto, category);

        bookmarkTagService.update(bookmark, requestDto);
    }

    @Transactional
    public void deleteBookmark(long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }

    private Bookmark findById(long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new BookmarkNotFoundException(String.valueOf(bookmarkId)));
    }

    private void validateBookmark(BookmarkRequest.Info requestDto, Category category) {
        if (!bookmarkRepository.existsByUrlAndCategory(requestDto.getUrl(), category)) {
            throw new BookmarkDuplicationException(requestDto.getUrl());
        }
    }
}
