package com.nobrain.bookmarking.domain.bookmark.service;

import com.nobrain.bookmarking.domain.auth.service.TokenService;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkResponse;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.bookmark.exception.BookmarkDuplicationException;
import com.nobrain.bookmarking.domain.bookmark.exception.BookmarkNotFoundException;
import com.nobrain.bookmarking.domain.bookmark.repository.BookmarkQueryRepository;
import com.nobrain.bookmarking.domain.bookmark.repository.BookmarkRepository;
import com.nobrain.bookmarking.domain.bookmark_tag.service.BookmarkTagService;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.category.exception.CategoryNotFoundException;
import com.nobrain.bookmarking.domain.category.repository.CategoryRepository;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import com.nobrain.bookmarking.domain.util.MetaImageCrawler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    private final TokenService tokenService;

    public List<BookmarkResponse.Info> getAllBookmarksByUsername(String username) {
        Long userId = findUserByUsername(username).getId();
        boolean isMe = isMe(userId);

        return bookmarkQueryRepository.findAllByUserId(userId, isMe).stream()
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());
    }

    public List<BookmarkResponse.Info> getBookmarksByCategory(String username, String categoryName) {
        User user = findUserByUsername(username);
        Category category = categoryRepository.findByUserAndName(user, categoryName).orElseThrow(() -> new CategoryNotFoundException(categoryName));
        boolean isMe = isMe(user.getId());

        return bookmarkQueryRepository.findAllByCategoryId(category.getId(), isMe).stream()
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());
    }

    public List<BookmarkResponse.Info> getStarredBookmarks(String username) {
        Long userId = findUserByUsername(username).getId();
        boolean isMe = isMe(userId);

        return bookmarkQueryRepository.findAllStarredBookmarksByUserId(userId, isMe).stream()
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());
    }

    public Long getStarredBookmarksCount(String username) {
        Long userId = findUserByUsername(username).getId();
        boolean isMe = isMe(userId);

        return bookmarkQueryRepository.findStarredBookmarksCountByUserId(userId, isMe);
    }

    public List<BookmarkResponse.Info> getPrivateBookmarks(String username) {
        Long userId = findUserByUsername(username).getId();

        return bookmarkQueryRepository.findPrivateBookmarksByUserId(userId).stream()
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());
    }

    public Long getPrivateBookmarksCount(String username) {
        Long userId = findUserByUsername(username).getId();

        return bookmarkQueryRepository.findPrivateBookmarksCountByUserId(userId);
    }

    public List<BookmarkResponse.Info> searchBookmarks(String keyword, String condition) {
        Long userId = tokenService.getId();
        boolean isMe = isMe(userId);

        return bookmarkQueryRepository.searchBookmarksByCondition(keyword, condition, userId, isMe).stream()
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createBookmark(String username, BookmarkRequest.Info requestDto) {
        String url = requestDto.getUrl();
        if (!url.contains("https://")) {
            requestDto.addHttpsToUrl(url);
        }

        User user = findUserByUsername(username);
        Category category = categoryRepository.findByUserAndName(user, requestDto.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException(requestDto.getCategoryName()));
        String metaImage = MetaImageCrawler.getMetaImageFromUrl(requestDto.getUrl());
        Bookmark bookmark = requestDto.toEntity(metaImage, category);

        validateBookmark(requestDto, category);
        bookmarkRepository.save(bookmark);

        if (requestDto.getTags() != null) {
            bookmarkTagService.saveTags(bookmark, requestDto.getTags());
        }
    }

    @Transactional
    public void updateBookmark(Long bookmarkId, BookmarkRequest.Info requestDto) {
        String url = requestDto.getUrl();
        if (!url.contains("https://")) {
            requestDto.addHttpsToUrl(url);
        }

        Bookmark bookmark = findById(bookmarkId);
        Category category = categoryRepository.findByName(requestDto.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException(requestDto.getCategoryName()));
        String metaImage = MetaImageCrawler.getMetaImageFromUrl(requestDto.getUrl());

        bookmark.update(requestDto, metaImage, category);

        bookmarkTagService.update(bookmark, requestDto);
    }

    @Transactional
    public void updateStarred(Long bookmarkId, Boolean isStarred) {
        Bookmark bookmark = findById(bookmarkId);

        if (bookmark.isStarred() != isStarred) {
            bookmark.changeStarred(isStarred);
        }
    }

    @Transactional
    public void updatePublic(Long bookmarkId, Boolean isPublic) {
        Bookmark bookmark = findById(bookmarkId);

        if (bookmark.isPublic() != isPublic) {
            bookmark.changePublic(isPublic);
        }
    }

    @Transactional
    public void updateAllBookmarksToPrivate(Long userId, String categoryName) {
        List<Bookmark> bookmarks = bookmarkQueryRepository.findBookmarksByUserIdAndCategoryName(userId, categoryName);
        bookmarks.forEach(bookmark -> {
            if (bookmark.isPublic()) {
                bookmark.changePublic(false);
            }
        });
    }

    @Transactional
    public void deleteBookmark(Long bookmarkId) {
        bookmarkRepository.deleteById(bookmarkId);
    }

    private Bookmark findById(Long bookmarkId) {
        return bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new BookmarkNotFoundException(String.valueOf(bookmarkId)));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByName(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    private boolean isMe(Long userId) {
        Long myId = tokenService.getId();
        return Objects.equals(userId, myId);
    }

    private void validateBookmark(BookmarkRequest.Info requestDto, Category category) {
        if (!bookmarkRepository.existsByUrlAndCategory(requestDto.getUrl(), category)) {
            throw new BookmarkDuplicationException(requestDto.getUrl());
        }
    }

    private BookmarkResponse.Info toBookmarkInfoDto(Bookmark bookmark) {
        return BookmarkResponse.Info.builder()
                .id(bookmark.getId())
                .url(bookmark.getUrl())
                .title(bookmark.getTitle())
                .description(bookmark.getDescription())
                .isPublic(bookmark.isPublic())
                .isStarred(bookmark.isStarred())
                .image(bookmark.getMetaImage())
                .createdAt(bookmark.getCreatedAt().toLocalDate())
                .modifiedAt(bookmark.getModifiedAt().toLocalDate())
                .build();
    }

}
