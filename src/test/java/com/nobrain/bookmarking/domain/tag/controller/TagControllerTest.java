package com.nobrain.bookmarking.domain.tag.controller;

import com.nobrain.bookmarking.docs.RestDocsTestSupport;
import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.tag.dto.TagResponse;
import com.nobrain.bookmarking.domain.user.entity.User;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
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

class TagControllerTest extends RestDocsTestSupport {

    private User user;
    private UserPayload payload;

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

        given(tokenProvider.validateToken(any(HttpServletRequest.class)))
                .willReturn(true);
    }

    @Test
    @DisplayName("유저의 전체 태그 조회 - 성공")
    void getAllTagsOfUser() throws Exception {
        // given
        given(tagService.getAllTagsOfUser(anyString()))
                .willReturn(List.of(TagResponse.Info.builder()
                                .tagId(TAG_ID)
                                .tagName(TAG_NAME)
                                .build()));

        // when
        ResultActions result = mockMvc.perform(
                get(BASE_URL + "/tags/users/{username}", user.getName())
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("username").description("유저 이름")
                                ),
                                responseFields(beneathPath("list[]").withSubsectionId("list"),
                                        fieldWithPath("tagId").type(JsonFieldType.NUMBER).description("태그 아이디"),
                                        fieldWithPath("tagName").type(JsonFieldType.STRING).description("태그 이름")
                                )
                        ));

        verify(tagService).getAllTagsOfUser(user.getName());
    }

    @Test
    void getTagsOfUserByBookmarkId() {
    }
}