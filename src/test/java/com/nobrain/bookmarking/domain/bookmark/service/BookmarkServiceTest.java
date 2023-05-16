package com.nobrain.bookmarking.domain.bookmark.service;

import com.nobrain.bookmarking.ServiceTest;
import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkResponse;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.bookmark.exception.BookmarkDuplicationException;
import com.nobrain.bookmarking.domain.bookmark.exception.BookmarkNotFoundException;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.category.exception.CategoryNotFoundException;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.global.security.PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nobrain.bookmarking.Constants.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class BookmarkServiceTest extends ServiceTest {

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private PasswordEncryptor encryptor;

    private User user;
    private UserPayload payload;
    private Category category;
    private Bookmark bookmark;

    @BeforeEach
    void setUp() {
        payload = UserPayload.builder()
                .userId(USER_ID)
                .username(USERNAME)
                .build();

        user = User.builder()
                .id(USER_ID)
                .name(USERNAME)
                .email(EMAIL)
                .password(encryptor.encrypt(PASSWORD))
                .profileImage(PROFILE_IMG)
                .categories(new ArrayList<>())
                .build();

        category = Category.builder()
                .id(CATEGORY_ID)
                .name(CATEGORY_NAME)
                .description(CATEGORY_DESCRIPTION)
                .isPublic(CATEGORY_PUBLIC)
                .user(user)
                .bookmarks(new ArrayList<>())
                .build();


        bookmark = Bookmark.builder()
                .id(BOOKMARK_ID)
                .url(URL)
                .title(TITLE)
                .description(DESCRIPTION)
                .isPublic(IS_PUBLIC)
                .isStarred(IS_STARRED)
                .category(category)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("북마크 리스트 조회 - 성공")
    void getBookmarksByUsername() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));
        given(bookmarkQueryRepository.findAllByUserId(anyLong(), anyBoolean()))
                .willReturn(List.of(bookmark));

        // when
        List<BookmarkResponse.Info> actual = bookmarkService.getBookmarksByUsername(payload, user.getName());

        // then
        List<BookmarkResponse.Info> expected = Stream.of(bookmark)
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

        verify(users).findByName(user.getName());
        verify(bookmarkQueryRepository).findAllByUserId(user.getId(), true);
    }

    @Test
    @DisplayName("북마크 리스트 조회 - 존재하지 않는 유저 실패")
    void getBookmarksByUsername_not_exists_user() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> bookmarkService.getBookmarksByUsername(payload, user.getName()))
                .isInstanceOf(UserNotFoundException.class);

        verify(users).findByName(user.getName());
    }

    @Test
    @DisplayName("카테고리에 속해 있는 북마크 리스트 조회 - 성공")
    void getBookmarksByCategory() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));
        given(categories.findByUserAndName(any(User.class), anyString()))
                .willReturn(Optional.of(category));
        given(bookmarkQueryRepository.findAllByCategoryId(anyLong(), anyBoolean()))
                .willReturn(List.of(bookmark));

        // when
        List<BookmarkResponse.Info> actual = bookmarkService.getBookmarksByCategory(payload, user.getName(), category.getName());

        // then
        List<BookmarkResponse.Info> expected = Stream.of(bookmark)
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

        verify(users).findByName(user.getName());
        verify(categories).findByUserAndName(user, category.getName());
        verify(bookmarkQueryRepository).findAllByCategoryId(category.getId(), true);
    }

    @Test
    @DisplayName("카테고리에 속해 있는 북마크 리스트 조회 - 존재하지 않는 카테고리 실패")
    void getBookmarksByCategory_not_exists_category() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));
        given(categories.findByUserAndName(any(User.class), anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> bookmarkService.getBookmarksByCategory(payload, user.getName(), category.getName()))
                .isInstanceOf(CategoryNotFoundException.class);

        verify(users).findByName(user.getName());
        verify(categories).findByUserAndName(user, category.getName());
    }

    @Test
    @DisplayName("즐겨찾기된 북마크 조회 - 성공")
    void getStarredBookmarks() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));
        given(bookmarkQueryRepository.findAllStarredBookmarksByUserId(anyLong(), anyBoolean()))
                .willReturn(List.of(bookmark));

        // when
        List<BookmarkResponse.Info> actual = bookmarkService.getStarredBookmarks(payload, user.getName());

        // then
        List<BookmarkResponse.Info> expected = Stream.of(bookmark)
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

        verify(users).findByName(user.getName());
        verify(bookmarkQueryRepository).findAllStarredBookmarksByUserId(user.getId(), true);
    }

    @Test
    @DisplayName("즐겨찾기된 북마크 개수 - 성공")
    void getStarredBookmarksCount() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));
        given(bookmarkQueryRepository.findStarredBookmarksCountByUserId(anyLong(), anyBoolean()))
                .willReturn(1L);

        // when
        Long actual = bookmarkService.getStarredBookmarksCount(payload, user.getName());

        // then
        Long expected = 1L;
        assertThat(actual).isEqualTo(expected);

        verify(users).findByName(user.getName());
        verify(bookmarkQueryRepository).findStarredBookmarksCountByUserId(category.getId(), true);
    }

    @Test
    @DisplayName("비공개 북마크 조회 - 성공")
    void getPrivateBookmarks() {
        // given
        given(bookmarkQueryRepository.findPrivateBookmarksByUserId(anyLong()))
                .willReturn(List.of(bookmark));

        // when
        List<BookmarkResponse.Info> actual = bookmarkService.getPrivateBookmarks(payload);

        // then
        List<BookmarkResponse.Info> expected = Stream.of(bookmark)
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

        verify(bookmarkQueryRepository).findPrivateBookmarksByUserId(user.getId());
    }

    @Test
    @DisplayName("비공개 북마크 개수 조회 - 성공")
    void getPrivateBookmarksCount() {
        // given
        given(bookmarkQueryRepository.findPrivateBookmarksCountByUserId(anyLong()))
                .willReturn(1L);

        // when
        Long actual = bookmarkService.getPrivateBookmarksCount(payload);

        // then
        Long expected = 1L;
        assertThat(actual).isEqualTo(expected);

        verify(bookmarkQueryRepository).findPrivateBookmarksCountByUserId(category.getId());
    }

    @Test
    @DisplayName("북마크 검색 - 성공")
    void searchBookmarks() {
        // given
        given(bookmarkQueryRepository.searchBookmarksByCondition(anyString(), anyString(), anyLong()))
                .willReturn(List.of(bookmark));

        // when
        List<BookmarkResponse.Info> actual = bookmarkService.searchBookmarks(payload, BOOKMARK_SEARCH_KEYWORD, BOOKMARK_SEARCH_CONDITION);

        // then
        List<BookmarkResponse.Info> expected = Stream.of(bookmark)
                .map(this::toBookmarkInfoDto)
                .collect(Collectors.toList());

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

        verify(bookmarkQueryRepository).searchBookmarksByCondition(BOOKMARK_SEARCH_KEYWORD, BOOKMARK_SEARCH_CONDITION, user.getId());
    }

    @Test
    @DisplayName("북마크 생성 - 성공")
    void createBookmark() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));
        given(categories.findByUserAndName(any(User.class), anyString()))
                .willReturn(Optional.of(category));
        given(bookmarks.existsByUrlAndCategory(anyString(), any(Category.class)))
                .willReturn(false);

        BookmarkRequest.Info request = new BookmarkRequest.Info(
                URL,
                TITLE,
                DESCRIPTION,
                IS_PUBLIC,
                IS_STARRED,
                CATEGORY_NAME,
                META_IMG,
                Collections.emptyList()
        );

        // when, then
        assertThatCode(() -> bookmarkService.createBookmark(payload, request))
                .doesNotThrowAnyException();
        assertThat(request.getUrl()).isEqualTo(HTTPS + URL);

        verify(users).findByName(user.getName());
        verify(categories).findByUserAndName(user, category.getName());
        verify(bookmarks).existsByUrlAndCategory(HTTPS + bookmark.getUrl(), category);
    }

    @Test
    @DisplayName("북마크 생성 - 같은 카테고리에 속한 중복 북마크 실패")
    void createBookmark_duplication_bookmark() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));
        given(categories.findByUserAndName(any(User.class), anyString()))
                .willReturn(Optional.of(category));
        given(bookmarks.existsByUrlAndCategory(anyString(), any(Category.class)))
                .willReturn(true);

        BookmarkRequest.Info request = new BookmarkRequest.Info(
                URL,
                TITLE,
                DESCRIPTION,
                IS_PUBLIC,
                IS_STARRED,
                CATEGORY_NAME,
                META_IMG,
                Collections.emptyList()
        );

        // when, then
        assertThatThrownBy(() -> bookmarkService.createBookmark(payload, request))
                .isInstanceOf(BookmarkDuplicationException.class);

        verify(users).findByName(user.getName());
        verify(categories).findByUserAndName(user, category.getName());
        verify(bookmarks).existsByUrlAndCategory(HTTPS + bookmark.getUrl(), category);
    }

    @Test
    @DisplayName("북마크 수정 - 성공")
    void updateBookmark() {
        // given
        given(bookmarks.findById(anyLong()))
                .willReturn(Optional.of(bookmark));
        given(categories.findByName(anyString()))
                .willReturn(Optional.of(category));

        BookmarkRequest.Info request = new BookmarkRequest.Info(
                CHANGE_URL,
                CHANGE_TITLE,
                CHANGE_DESCRIPTION,
                IS_PUBLIC,
                IS_STARRED,
                CATEGORY_NAME,
                META_IMG,
                Collections.emptyList()
        );

        // when, then
        Bookmark expected = Bookmark.builder()
                .url(HTTPS + CHANGE_URL)
                .title(CHANGE_TITLE)
                .description(CHANGE_DESCRIPTION)
                .category(category)
                .build();

        assertThatCode(() -> bookmarkService.updateBookmark(BOOKMARK_ID, request))
                .doesNotThrowAnyException();
        assertThat(bookmark).usingRecursiveComparison()
                .comparingOnlyFields("url", "title", "description")
                .isEqualTo(expected);

        verify(bookmarks).findById(bookmark.getId());
        verify(categories).findByName(category.getName());
    }

    @Test
    @DisplayName("북마크 수정 - 존재하지 않는 북마크 실패")
    void updateBookmark_not_exists_bookmark() {
        // given
        given(bookmarks.findById(anyLong()))
                .willReturn(Optional.empty());

        BookmarkRequest.Info request = new BookmarkRequest.Info(
                CHANGE_URL,
                CHANGE_TITLE,
                CHANGE_DESCRIPTION,
                IS_PUBLIC,
                IS_STARRED,
                CATEGORY_NAME,
                META_IMG,
                Collections.emptyList()
        );

        // when, then
        assertThatThrownBy(() -> bookmarkService.updateBookmark(BOOKMARK_ID, request))
                .isInstanceOf(BookmarkNotFoundException.class);

        verify(bookmarks).findById(bookmark.getId());
    }

    @Test
    @DisplayName("북마크 즐겨찾기 on/off - 성공")
    void updateStarred() {
        // given
        given(bookmarks.findById(anyLong()))
                .willReturn(Optional.of(bookmark));

        // when
        bookmarkService.updateStarred(bookmark.getId(), IS_NOT_STARRED);

        // then
        assertThat(bookmark.isStarred()).isFalse();

        verify(bookmarks).findById(bookmark.getId());
    }

    @Test
    @DisplayName("북마크 공개/비공개 수정 - 성공")
    void updatePublic() {
        // given
        given(bookmarks.findById(anyLong()))
                .willReturn(Optional.of(bookmark));

        // when
        bookmarkService.updatePublic(bookmark.getId(), IS_PRIVATE);

        // then
        assertThat(bookmark.isPublic()).isFalse();

        verify(bookmarks).findById(bookmark.getId());
    }

    @Test
    @DisplayName("카테고리 비공개 변경시 해당 카테고리에 속해 있는 북마크 모두 비공개로 변경 - 성공")
    void updateBookmarksToPrivate() {
        // given
        given(bookmarkQueryRepository.findPublicBookmarksByUserIdAndCategoryName(anyLong(), anyString()))
                .willReturn(List.of(bookmark));

        // when
        bookmarkService.updateBookmarksToPrivate(payload, category.getName());

        // then
        List<Bookmark> expected = List.of(
                Bookmark.builder()
                        .category(category)
                        .isPublic(IS_PRIVATE)
                        .build());

        assertThat(List.of(bookmark)).usingRecursiveComparison()
                .comparingOnlyFields("isPublic")
                .isEqualTo(expected);

        verify(bookmarkQueryRepository).findPublicBookmarksByUserIdAndCategoryName(user.getId(), category.getName());
    }

    @Test
    @DisplayName("북마크 삭제 - 성공")
    void deleteBookmark() {
        assertThatCode(() -> bookmarkService.deleteBookmark(bookmark.getId()))
                .doesNotThrowAnyException();
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