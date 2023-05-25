package com.nobrain.bookmarking.domain.tag.service;

import com.nobrain.bookmarking.ServiceTest;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.bookmark_tag.entity.BookmarkTag;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.tag.dto.TagResponse;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import com.nobrain.bookmarking.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.nobrain.bookmarking.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class TagServiceTest extends ServiceTest {

    @Autowired
    private TagService tagService;
    private User user;
    private Category category;
    private Bookmark bookmark;
    private BookmarkTag bookmarkTag;
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

        bookmarkTag = BookmarkTag.builder()
                .tag(tag)
                .bookmark(bookmark)
                .build();
    }

    @Test
    @DisplayName("태그 찾기 - 성공")
    void findTags() {
        // given
        given(tags.findByName(anyString()))
                .willReturn(Optional.of(tag));
        given(tags.save(any(Tag.class)))
                .willReturn(tag);

        // when
        Tag actual = tagService.findOrCreateTags(tag.getName());

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(tag);
        verify(tags).findByName(tag.getName());
        verify(tags).save(tag);
    }

    @Test
    @DisplayName("태그 만들기 - 성공")
    void CreateTags() {
        // given
        given(tags.findByName(anyString()))
                .willReturn(Optional.empty());
        given(tags.save(any(Tag.class)))
                .willReturn(tag);
        // when
        Tag actual = tagService.findOrCreateTags(tag.getName());

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(tag);
        verify(tags).findByName(tag.getName());
        verify(tags).save(any(Tag.class));
    }

    @Test
    @DisplayName("태그 전체 삭제 - 성공")
    void deleteAllByBookmarkTags() {
        // given
        List<BookmarkTag> bookmarkTagList = List.of(bookmarkTag);

        // when, then
        assertThatCode(() -> tagService.deleteAllByBookmarkTags(bookmarkTagList))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유저 전체 태그 조회 - 성공")
    void getAllTagsOfUser() {
        // given
        given(tagQueryRepository.findAllByUser(anyString()))
                .willReturn(List.of(tag));

        // when
        List<TagResponse.Info> actual = tagService.getAllTagsOfUser(user.getName());

        //then
        List<TagResponse.Info> expected = List.of(toTagInfoDto(tag));

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

        verify(tagQueryRepository).findAllByUser(user.getName());
    }

    @Test
    @DisplayName("북마크 태그 조회 - 성공")
    void getTagsOfUserByBookmarkId() {
        // given
        given(tagQueryRepository.findTagsByBookmarkId(anyString(), anyLong()))
                .willReturn(List.of(tag));

        // when
        List<TagResponse.Info> actual = tagService.getTagsOfUserByBookmarkId(user.getName(), bookmark.getId());

        // then
        List<TagResponse.Info> expected = List.of(toTagInfoDto(tag));

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);
        verify(tagQueryRepository).findTagsByBookmarkId(user.getName(), bookmark.getId());
    }

    private TagResponse.Info toTagInfoDto(Tag tag) {
        return TagResponse.Info.builder()
                .tagId(tag.getId())
                .tagName(tag.getName())
                .build();
    }
}