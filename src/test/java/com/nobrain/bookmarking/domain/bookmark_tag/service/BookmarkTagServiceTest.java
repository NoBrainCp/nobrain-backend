package com.nobrain.bookmarking.domain.bookmark_tag.service;

import com.nobrain.bookmarking.ServiceTest;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.bookmark_tag.dto.projection.BookmarkTagProjection;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import com.nobrain.bookmarking.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.nobrain.bookmarking.Constants.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class BookmarkTagServiceTest extends ServiceTest {

    @Autowired
    private BookmarkTagService bookmarkTagService;
    private User user;
    private Category category;
    private Bookmark bookmark;
    private Tag tag;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(USER_ID)
                .name(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .profileImage(PROFILE_IMG)
                .categories(new ArrayList<>())
                .build();

        tag = Tag.builder()
                .id(TAG_ID)
                .name(TAG_NAME)
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
    @DisplayName("복마크 태그 저장 - 성공")
    void saveTags() {
        // given
        List<String> tagList = List.of(TAG_NAME);

        //when, then
        assertThatCode(() -> bookmarkTagService.saveTags(bookmark, tagList))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("북마크 태그 수정 - 성공")
    void update() {
        // given
        BookmarkRequest.Info bookmarkRequestInfo = toBookmarkInfoDto(bookmark);

        // when, then
        assertThatCode(() -> bookmarkTagService.update(bookmark, bookmarkRequestInfo))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유저 전체 북마크 태그 조회 - 성공")
    void getAllBookmarkTags() {
        // given
        given(bookmarkTagQueryRepository.findAllBookmarkTagsByUserId(anyString()))
                .willReturn(List.of(toBookmarkAntTagDto(bookmark, tag)));
        // when, then
        assertThatCode(() -> bookmarkTagService.getAllBookmarkTags(user.getName()))
                .doesNotThrowAnyException();

        verify(bookmarkTagQueryRepository).findAllBookmarkTagsByUserId(user.getName());
    }

    @Test
    @DisplayName("유저 전체 태그 조회 - 성공")
    void getBookmarkTagsByTagList() {
        // given
        given(bookmarkTagQueryRepository.findBookmarkTagsByUserIdAndTagList(anyString(), anyList()))
                .willReturn(List.of(toBookmarkAntTagDto(bookmark, tag)));

        // when, then
        assertThatCode(() -> bookmarkTagService.getBookmarkTagsByTagList(user.getName(), List.of(tag.getId())))
                .doesNotThrowAnyException();
        verify(bookmarkTagQueryRepository).findBookmarkTagsByUserIdAndTagList(user.getName(), List.of(tag.getId()));
    }
    private BookmarkRequest.Info toBookmarkInfoDto(Bookmark bookmark) {
        return BookmarkRequest.Info.builder()
                .url(bookmark.getUrl())
                .title(bookmark.getTitle())
                .description(bookmark.getDescription())
                .isPublic(bookmark.isPublic())
                .isStarred(bookmark.isStarred())
                .categoryName(category.getName())
                .tags(List.of(tag.getName()))
                .build();
    }
    private BookmarkTagProjection.BookmarkAndTag toBookmarkAntTagDto(Bookmark bookmark, Tag tag) {
        return BookmarkTagProjection.BookmarkAndTag.builder()
                .bookmark(bookmark)
                .tag(tag)
                .build();
    }
}