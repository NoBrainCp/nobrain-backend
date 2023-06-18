package com.nobrain.bookmarking.domain.bookmark.service;

import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
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

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BookmarkRepository bookmarkRepository;
    private final BookmarkQueryRepository bookmarkQueryRepository;
    private final BookmarkTagService bookmarkTagService;

    public List<BookmarkResponse.Info> getBookmarksByUsername(UserPayload myPayload, String username) {
        Long userId = findUserByUsername(username).getId();
        boolean isMe = isMe(myPayload.getUserId(), userId);

        return bookmarkQueryRepository.findAllByUserId(userId, isMe).stream()
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());
    }

    public List<BookmarkResponse.Info> getBookmarksByCategory(UserPayload myPayload, String username, String categoryName) {
        User user = findUserByUsername(username);
        Category category = categoryRepository.findByUserAndName(user, categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));
        boolean isMe = isMe(myPayload.getUserId(), user.getId());

        return bookmarkQueryRepository.findAllByCategoryId(category.getId(), isMe).stream()
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());
    }

    public List<BookmarkResponse.Info> getStarredBookmarks(UserPayload myPayload, String username) {
        Long userId = findUserByUsername(username).getId();
        boolean isMe = isMe(myPayload.getUserId(), userId);

        return bookmarkQueryRepository.findAllStarredBookmarksByUserId(userId, isMe).stream()
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());
    }

    public Long getStarredBookmarksCount(UserPayload myPayload, String username) {
        Long userId = findUserByUsername(username).getId();
        boolean isMe = isMe(myPayload.getUserId(), userId);

        return bookmarkQueryRepository.findStarredBookmarksCountByUserId(userId, isMe);
    }

    public List<BookmarkResponse.Info> getPrivateBookmarks(UserPayload myPayload) {
        return bookmarkQueryRepository.findPrivateBookmarksByUserId(myPayload.getUserId()).stream()
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());
    }

    public Long getPrivateBookmarksCount(UserPayload myPayload) {
        return bookmarkQueryRepository.findPrivateBookmarksCountByUserId(myPayload.getUserId());
    }

    public List<BookmarkResponse.Info> searchBookmarks(UserPayload myPayload, String keyword, String condition) {
        return bookmarkQueryRepository.searchBookmarksByCondition(keyword, condition, myPayload.getUserId()).stream()
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void createBookmark(UserPayload myPayload, BookmarkRequest.Info requestDto) {
        String url = requestDto.getUrl();
        if (!url.contains("https://")) {
            requestDto.addHttpsToUrl(url);
        }

        User user = findUserByUsername(myPayload.getUsername());
        Category category = categoryRepository.findByUserAndName(user, requestDto.getCategoryName())
                .orElseThrow(() -> new CategoryNotFoundException(requestDto.getCategoryName()));
        String metaImage = MetaImageCrawler.getMetaImageFromUrl(requestDto.getUrl());
        Bookmark bookmark = requestDto.toEntity(metaImage, category);

//        validateBookmark(requestDto.getUrl(), category);
        bookmarkRepository.save(bookmark);

        if (requestDto.getTags() != null) {
            bookmarkTagService.saveTags(bookmark, requestDto.getTags());
        }
    }

    @Transactional
    public void updateBookmark(UserPayload myPayload, Long bookmarkId, BookmarkRequest.Info requestDto) {
        String url = requestDto.getUrl();
        if (!url.contains("https://")) {
            requestDto.addHttpsToUrl(url);
        }
        User user = findUserByUsername(myPayload.getUsername());
        Bookmark bookmark = findById(bookmarkId);
        Category category = categoryRepository.findByUserAndName(user, requestDto.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException(requestDto.getCategoryName()));
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

    //TODO ** Update Query - public인 북마크만 조회 **
    @Transactional
    public void updateBookmarksToPrivate(UserPayload myPayload, String categoryName) {
        List<Bookmark> bookmarks = bookmarkQueryRepository.findPublicBookmarksByUserIdAndCategoryName(myPayload.getUserId(), categoryName);
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

    private boolean isMe(Long myId, Long userId) {
        return Objects.equals(myId, userId);
    }

    private void validateBookmark(String url, Category category) {
        if (bookmarkRepository.existsByUrlAndCategory(url, category)) {
            throw new BookmarkDuplicationException(url);
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
