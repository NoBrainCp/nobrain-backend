package com.nobrain.bookmarking.domain.bookmark_tag.controller;

import com.nobrain.bookmarking.docs.RestDocsTestSupport;
import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkResponse;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.bookmark_tag.dto.BookmarkTagResponse;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import com.nobrain.bookmarking.domain.user.entity.User;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.nobrain.bookmarking.Constants.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookmarkTagControllerTest extends RestDocsTestSupport {

    private final User user = User.builder()
            .id(USER_ID)
            .name(USERNAME)
            .email(EMAIL)
            .password(PASSWORD)
            .profileImage(PROFILE_IMG)
            .categories(new ArrayList<>())
            .build();

    private final UserPayload payload = UserPayload.builder()
            .userId(USER_ID)
            .username(USERNAME)
            .build();

    private final Category category = Category.builder()
            .id(CATEGORY_ID)
            .name(CATEGORY_NAME)
            .description(CATEGORY_DESCRIPTION)
            .isPublic(CATEGORY_PUBLIC)
            .user(user)
            .bookmarks(new ArrayList<>())
            .build();

    private final Bookmark bookmark = Bookmark.builder()
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

    private final Tag tag = Tag.builder()
            .id(TAG_ID)
            .name(TAG_NAME)
            .build();

    @Test
    @DisplayName("유저의 모든 북마크 태그를 조회 - 성공")
    void getAllBookmarkTags() throws Exception {
        // given
        given(tokenProvider.validateToken(any(HttpServletRequest.class)))
                .willReturn(true);
        given(bookmarkTagService.getAllBookmarkTags(anyString()))
                .willReturn(List.of(BookmarkTagResponse.Info.builder()
                        .bookmarks(List.of(bookmark))
                        .tag(tag)
                        .build()));

        // when
        ResultActions result = mockMvc.perform(
                get(BASE_URL + "/bookmark-tags/user/{username}", user.getName())
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("username").description("유저 이름")
                                ),
                                responseFields(beneathPath("list[]").withSubsectionId("list"),
                                        fieldWithPath("tag").type(JsonFieldType.OBJECT).description("태그"),
                                        fieldWithPath("tag.id").type(JsonFieldType.NUMBER).description("태그 아이디"),
                                        fieldWithPath("tag.name").type(JsonFieldType.STRING).description("태그 이름"),
                                        fieldWithPath("bookmarks[]").type(JsonFieldType.ARRAY).description("북마크 리스트"),
                                        fieldWithPath("bookmarks[].id").type(JsonFieldType.NUMBER).description("북마크 아이디"),
                                        fieldWithPath("bookmarks[].url").type(JsonFieldType.STRING).description("북마크 URL"),
                                        fieldWithPath("bookmarks[].title").type(JsonFieldType.STRING).description("북마크 제목"),
                                        fieldWithPath("bookmarks[].description").type(JsonFieldType.STRING).description("북마크 설명"),
                                        fieldWithPath("bookmarks[].metaImage").type(JsonFieldType.STRING).description("북마크 메타 이미지").optional(),
                                        fieldWithPath("bookmarks[].public").type(JsonFieldType.BOOLEAN).description("북마크 공개"),
                                        fieldWithPath("bookmarks[].starred").type(JsonFieldType.BOOLEAN).description("북마크 즐겨찾기"),
                                        fieldWithPath("bookmarks[].createdAt").type(JsonFieldType.STRING).description("북마크 생성 시간"),
                                        fieldWithPath("bookmarks[].modifiedAt").type(JsonFieldType.STRING).description("북마크 수정 시간")
                                )
                        ));

        verify(bookmarkTagService).getAllBookmarkTags(user.getName());
    }

    @Test
    @DisplayName("태그 아이디 리스트를 통한 전체 북마크 조회 - 성공")
    void getBookmarkTagsByTagList() throws Exception {
        // given
        given(tokenProvider.validateToken(any(HttpServletRequest.class)))
                .willReturn(true);
        given(bookmarkTagService.getBookmarkTagsByTagList(anyString(), anyList()))
                .willReturn(List.of(BookmarkResponse.Info.builder()
                        .id(bookmark.getId())
                        .url(bookmark.getUrl())
                        .title(bookmark.getTitle())
                        .description(bookmark.getDescription())
                        .isPublic(bookmark.isPublic())
                        .isStarred(bookmark.isStarred())
                        .image(bookmark.getMetaImage())
                        .createdAt(bookmark.getCreatedAt().toLocalDate())
                        .modifiedAt(bookmark.getModifiedAt().toLocalDate())
                        .build()));

        // when
        ResultActions result = mockMvc.perform(
                get(BASE_URL + "/bookmark-tags/users/{username}/bookmarks", user.getName())
                        .param("tagIds", "1")
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("username").description("유저 이름")
                                ),
                                requestParameters(
                                        parameterWithName("tagIds").description("태그 아이디 리스트")
                                ),
                                responseFields(beneathPath("list[]").withSubsectionId("list"),
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("북마크 아이디"),
                                        fieldWithPath("url").type(JsonFieldType.STRING).description("북마크 URL"),
                                        fieldWithPath("title").type(JsonFieldType.STRING).description("북마크 제목"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("북마크 설명"),
                                        fieldWithPath("image").type(JsonFieldType.STRING).description("북마크 메타 이미지").optional(),
                                        fieldWithPath("public").type(JsonFieldType.BOOLEAN).description("북마크 공개"),
                                        fieldWithPath("starred").type(JsonFieldType.BOOLEAN).description("북마크 즐겨찾기"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("북마크 생성 시간"),
                                        fieldWithPath("modifiedAt").type(JsonFieldType.STRING).description("북마크 수정 시간")
                                )
                        ));
    }
}