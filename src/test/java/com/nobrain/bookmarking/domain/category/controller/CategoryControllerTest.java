package com.nobrain.bookmarking.domain.category.controller;

import com.nobrain.bookmarking.docs.RestDocsTestSupport;
import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.category.dto.CategoryResponse;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.user.entity.User;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.nobrain.bookmarking.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest extends RestDocsTestSupport {

    private User user;
    private UserPayload payload;
    private Category category;

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

        payload = UserPayload.builder()
                .userId(USER_ID)
                .username(USERNAME)
                .build();

        category = Category.builder()
                .id(CATEGORY_ID)
                .name(CATEGORY_NAME)
                .description(CATEGORY_DESCRIPTION)
                .isPublic(CATEGORY_PUBLIC)
                .user(user)
                .bookmarks(new ArrayList<>())
                .build();

        given(tokenProvider.validateToken(any(HttpServletRequest.class)))
                .willReturn(true);
        given(tokenExtractor.extract(any(HttpServletRequest.class)))
                .willReturn(ACCESS_TOKEN);
        given(tokenProvider.getPayload(ACCESS_TOKEN))
                .willReturn(payload);
    }

    @Test
    @DisplayName("카테고리 헤더 조회 - 성공")
    void getCategory() throws Exception {
        // given
        given(categoryService.getCategory(anyString(), anyString()))
                .willReturn(CategoryResponse.Header.toDto(category));

        // when
        ResultActions result = mockMvc.perform(
                get(BASE_URL + "/categories/{categoryName}/users/{username}",
                        category.getName(), user.getName())
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("categoryName").description("카테고리 이름"),
                                        parameterWithName("username").description("유저 이름")
                                ),
                                responseFields(beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("id").description("카테고리 아이디"),
                                        fieldWithPath("name").description("카테고리 이름"),
                                        fieldWithPath("description").description("카테고리 설명"),
                                        fieldWithPath("public").description("카테고리 공개 여부")
                                )
                        )
                );

        verify(categoryService).getCategory(user.getName(), category.getName());
    }

    @Test
    @DisplayName("유저의 카테고리 목록 조회 - 성공")
    void getCategories() throws Exception {
        // given
        given(categoryService.getCategories(any(), anyString()))
                .willReturn(List.of(
                        CategoryResponse.Info.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .isPublic(category.isPublic())
                        .count(CATEGORY_BOOKMARK_COUNT)
                        .build()));

        // when
        ResultActions result = mockMvc.perform(
                get(BASE_URL + "/categories/users/{username}", user.getName())
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER));

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("username").description("유저 이름")
                        ),
                        responseFields(beneathPath("list[]").withSubsectionId("list"),
                                fieldWithPath("id").description("카테고리 아이디"),
                                fieldWithPath("name").description("카테고리 이름"),
                                fieldWithPath("description").description("카테고리 설명"),
                                fieldWithPath("public").description("카테고리 공개 여부"),
                                fieldWithPath("count").description("카테고리의 북마크 개수")
                        )
                ));

        verify(categoryService).getCategories(payload, user.getName());
    }

    @Test
    void getCategoryByBookmarkId() {
    }

    @Test
    void getCategoryIsPublic() {
    }

    @Test
    void addCategory() {
    }

    @Test
    void updateCategory() {
    }

    @Test
    void deleteCategory() {
    }
}