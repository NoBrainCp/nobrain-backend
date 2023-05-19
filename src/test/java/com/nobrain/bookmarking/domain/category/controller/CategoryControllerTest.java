package com.nobrain.bookmarking.domain.category.controller;

import com.nobrain.bookmarking.docs.RestDocsTestSupport;
import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.category.dto.CategoryRequest;
import com.nobrain.bookmarking.domain.category.dto.CategoryResponse;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.user.entity.User;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.nobrain.bookmarking.Constants.*;
import static com.nobrain.bookmarking.config.RestDocsConfig.field;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("카테고리 아이디"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름"),
                                        fieldWithPath("description").type(JsonFieldType.STRING).description("카테고리 설명"),
                                        fieldWithPath("public").type(JsonFieldType.BOOLEAN).description("카테고리 공개 여부")
                                )
                        )
                );

        verify(categoryService).getCategory(user.getName(), category.getName());
    }

    @Test
    @DisplayName("유저의 카테고리 목록 조회 - 성공")
    void getCategories() throws Exception {
        // given
        given(categoryService.getCategories(any(UserPayload.class), anyString()))
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
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("카테고리 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("카테고리 설명"),
                                fieldWithPath("public").type(JsonFieldType.BOOLEAN).description("카테고리 공개 여부"),
                                fieldWithPath("count").type(JsonFieldType.NUMBER).description("카테고리의 북마크 개수")
                        )
                ));

        verify(categoryService).getCategories(payload, user.getName());
    }

    @Test
    @DisplayName("북마크를 통한 카테고리 조회 - 성공")
    void getCategoryByBookmarkId() throws Exception {
        // given
        given(categoryService.getCategoryByBookmarkId(anyLong()))
                .willReturn(CategoryResponse.Info.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .isPublic(category.isPublic())
                        .count(CATEGORY_BOOKMARK_COUNT)
                        .build());

        // when
        ResultActions result = mockMvc.perform(
                get(BASE_URL + "/categories/bookmarks/{bookmarkId}", BOOKMARK_ID)
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER));

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("bookmarkId").description("북마크 아이디")
                        ),
                        responseFields(beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("카테고리 아이디"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리 이름"),
                                fieldWithPath("description").type(JsonFieldType.STRING).description("카테고리 설명"),
                                fieldWithPath("public").type(JsonFieldType.BOOLEAN).description("카테고리 공개 여부"),
                                fieldWithPath("count").type(JsonFieldType.NUMBER).description("카테고리의 북마크 개수")
                        )
                ));

        verify(categoryService).getCategoryByBookmarkId(BOOKMARK_ID);
    }

    @Test
    @DisplayName("카테고리의 공개 여부 조회 - 성공")
    void getCategoryIsPublic() throws Exception {
        // given
        given(categoryService.getCategoryIsPublic(any(UserPayload.class), anyString()))
                .willReturn(category.isPublic());

        // when
        ResultActions result = mockMvc.perform(
                get(BASE_URL + "/categories/{categoryName}/public", category.getName())
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER));

        // then
        result.andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("categoryName").description("카테고리 이름")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 성공 여부"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.BOOLEAN).description("카테고리 공개 여부")
                        )
                ));

        verify(categoryService).getCategoryIsPublic(payload, category.getName());
    }

    @Test
    void addCategory() throws Exception {
        // given
        given(categoryService.create(any(UserPayload.class), any(CategoryRequest.Info.class)))
                .willReturn(category.getName());

        CategoryRequest.Info addCategoryRequest = new CategoryRequest.Info(
                category.getName(),
                category.getDescription(),
                category.isPublic());

        // when
        ResultActions result = mockMvc.perform(
                post(BASE_URL + "/categories")
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER)
                        .content(createJson(addCategoryRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isCreated())
                .andDo(restDocs.document(
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                        .description("카테고리 이름")
                                        .attributes(field("constraints", "최대 10글자")),
                                fieldWithPath("description").type(JsonFieldType.STRING)
                                        .description("카테고리 설명")
                                        .attributes(field("constraints", "최대 20글자"))
                                        .optional(),
                                fieldWithPath("isPublic").type(JsonFieldType.BOOLEAN)
                                        .description("카테고리 공개여부")
                                        .attributes(field("constraints", "true/false"))
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("응답 성공 여부"),
                                fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
                                fieldWithPath("data").type(JsonFieldType.STRING).description("카테고리 이름")
                        )
                ));
    }

    @Test
    void updateCategory() {
    }

    @Test
    void deleteCategory() {
    }
}