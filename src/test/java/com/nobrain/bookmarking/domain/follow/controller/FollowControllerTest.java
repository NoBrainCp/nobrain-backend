package com.nobrain.bookmarking.domain.follow.controller;

import com.nobrain.bookmarking.docs.RestDocsTestSupport;
import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.follow.dto.FollowResponse;
import com.nobrain.bookmarking.domain.user.entity.User;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Disabled;
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

class FollowControllerTest extends RestDocsTestSupport {

    private final User fromUser = User.builder()
            .id(USER_ID)
            .name(USERNAME)
            .email(EMAIL)
            .password(PASSWORD)
            .profileImage(PROFILE_IMG)
            .categories(new ArrayList<>())
            .build();

    private final User toUser = User.builder()
            .id(TO_USER_ID)
            .name(TO_USERNAME)
            .email(TO_USER_EMAIL)
            .password(TO_USER_PASSWORD)
            .profileImage(TO_USER_PROFILE_IMG)
            .categories(new ArrayList<>())
            .build();

    private final UserPayload fromUserPayload = UserPayload.builder()
            .userId(USER_ID)
            .username(USERNAME)
            .build();

    private final UserPayload toUserPayload = UserPayload.builder()
            .userId(TO_USER_ID)
            .username(TO_USERNAME)
            .build();

    @Test
    @DisplayName("유저의 팔로우, 팔로잉 수 조회 - 성공")
    void getFollowCount() throws Exception {
        // given
        given(tokenProvider.validateToken(any(HttpServletRequest.class)))
                .willReturn(true);
        given(followService.getFollowCount(anyString()))
                .willReturn(FollowResponse.FollowCount.builder()
                        .followerCnt(FOLLOWER_COUNT)
                        .followingCnt(FOLLOWING_COUNT)
                        .build());

        // when
        ResultActions result = mockMvc.perform(
                get(BASE_URL + "/users/{username}/follow-cnt", fromUser.getName())
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("username").description("유저 이름")
                                ),
                                responseFields(beneathPath("data").withSubsectionId("data"),
                                        fieldWithPath("followerCnt").type(JsonFieldType.NUMBER).description("팔로워 수"),
                                        fieldWithPath("followingCnt").type(JsonFieldType.NUMBER).description("팔로잉 수")
                                )
                        )
                );

        verify(tokenProvider).validateToken(any(HttpServletRequest.class));
        verify(followService).getFollowCount(fromUser.getName());
    }

    @Test
    @DisplayName("팔로워 리스트 조회 - 성공")
    void getFollowerList() throws Exception {
        // given
        given(tokenProvider.validateToken(any(HttpServletRequest.class)))
                .willReturn(true);
        given(followService.getFollowerList(anyString()))
                .willReturn(List.of(FollowResponse.Info.toResponse(fromUser)));

        // when
        ResultActions result = mockMvc.perform(
                get(BASE_URL + "/users/{username}/followers", toUser.getName())
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("username").description("유저 이름")
                                ),
                                responseFields(beneathPath("list[]").withSubsectionId("list"),
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 아이디"),
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("유저 이름"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("유저 프로필 이미지")
                                )
                        )
                );

        verify(tokenProvider).validateToken(any(HttpServletRequest.class));
        verify(followService).getFollowerList(toUser.getName());
    }

    @Test
    @DisplayName("팔로잉 리스트 조회 - 성공")
    void getFollowingList() throws Exception {
        // given
        given(tokenProvider.validateToken(any(HttpServletRequest.class)))
                .willReturn(true);
        given(followService.getFollowingList(anyString()))
                .willReturn(List.of(FollowResponse.Info.toResponse(toUser)));

        // when
        ResultActions result = mockMvc.perform(
                get(BASE_URL + "/users/{username}/followings", fromUser.getName())
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("username").description("유저 이름")
                                ),
                                responseFields(beneathPath("list[]").withSubsectionId("list"),
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 아이디"),
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("유저 이름"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("유저 프로필 이미지")
                                )
                        )
                );

        verify(tokenProvider).validateToken(any(HttpServletRequest.class));
        verify(followService).getFollowingList(fromUser.getName());
    }

    @Test
    @DisplayName("팔로워 카드 리스트 조회 - 성공")
    void getFollowerCardList() throws Exception {
        // given
        List<FollowResponse.FollowCard> followerCardList = List.of(FollowResponse.FollowCard.builder()
                .userId(fromUser.getId())
                .username(fromUser.getName())
                .profileImage(fromUser.getProfileImage())
                .bookmarkCount(BOOKMARK_COUNT)
                .followerCount((long) FOLLOWER_COUNT)
                .followingCount((long) FOLLOWING_COUNT)
                .isFollow(IS_FOLLOW)
                .build());

        given(tokenProvider.validateToken(any(HttpServletRequest.class)))
                .willReturn(true);
        given(tokenProvider.getPayload(anyString()))
                .willReturn(toUserPayload);
        given(tokenExtractor.extract(any(HttpServletRequest.class)))
                .willReturn(ACCESS_TOKEN);
        given(followService.getFollowerCardList(any(UserPayload.class), anyString()))
                .willReturn(followerCardList);

        // when
        ResultActions result = mockMvc.perform(
                get(BASE_URL + "/users/{username}/follower-cards", fromUser.getName())
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("username").description("유저 이름")
                                ),
                                responseFields(beneathPath("list[]").withSubsectionId("list"),
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 아이디"),
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("유저 이름"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("유저 프로필 이미지"),
                                        fieldWithPath("bookmarkCount").type(JsonFieldType.NUMBER).description("북마크 개수"),
                                        fieldWithPath("followerCount").type(JsonFieldType.NUMBER).description("팔로워 수"),
                                        fieldWithPath("followingCount").type(JsonFieldType.NUMBER).description("팔로잉 수"),
                                        fieldWithPath("isFollow").type(JsonFieldType.BOOLEAN).description("팔로우 여부")
                                )
                        )
                );

        verify(tokenProvider).validateToken(any(HttpServletRequest.class));
        verify(tokenProvider).getPayload(ACCESS_TOKEN);
        verify(tokenExtractor).extract(any(HttpServletRequest.class));
        verify(followService).getFollowerCardList(toUserPayload, fromUser.getName());
    }

    @Test
    @DisplayName("팔로잉 카드 리스트 조회 - 성")
    void getFollowingCardList() throws Exception {
        // given
        List<FollowResponse.FollowCard> followingCardList = List.of(FollowResponse.FollowCard.builder()
                .userId(toUser.getId())
                .username(toUser.getName())
                .profileImage(toUser.getProfileImage())
                .bookmarkCount(BOOKMARK_COUNT)
                .followerCount((long) FOLLOWER_COUNT)
                .followingCount((long) FOLLOWING_COUNT)
                .isFollow(IS_FOLLOW)
                .build());

        given(tokenProvider.validateToken(any(HttpServletRequest.class)))
                .willReturn(true);
        given(tokenProvider.getPayload(anyString()))
                .willReturn(fromUserPayload);
        given(tokenExtractor.extract(any(HttpServletRequest.class)))
                .willReturn(ACCESS_TOKEN);
        given(followService.getFollowingCardList(any(UserPayload.class), anyString()))
                .willReturn(followingCardList);

        // when
        ResultActions result = mockMvc.perform(
                get(BASE_URL + "/users/{username}/following-cards", toUser.getName())
                        .header(HttpHeaders.AUTHORIZATION, AUTHORIZATION_HEADER));

        // then
        result.andExpect(status().isOk())
                .andDo(
                        restDocs.document(
                                pathParameters(
                                        parameterWithName("username").description("유저 이름")
                                ),
                                responseFields(beneathPath("list[]").withSubsectionId("list"),
                                        fieldWithPath("userId").type(JsonFieldType.NUMBER).description("유저 아이디"),
                                        fieldWithPath("username").type(JsonFieldType.STRING).description("유저 이름"),
                                        fieldWithPath("profileImage").type(JsonFieldType.STRING).description("유저 프로필 이미지"),
                                        fieldWithPath("bookmarkCount").type(JsonFieldType.NUMBER).description("북마크 개수"),
                                        fieldWithPath("followerCount").type(JsonFieldType.NUMBER).description("팔로워 수"),
                                        fieldWithPath("followingCount").type(JsonFieldType.NUMBER).description("팔로잉 수"),
                                        fieldWithPath("isFollow").type(JsonFieldType.BOOLEAN).description("팔로우 여부")
                                )
                        )
                );

        verify(tokenProvider).validateToken(any(HttpServletRequest.class));
        verify(tokenProvider).getPayload(ACCESS_TOKEN);
        verify(tokenExtractor).extract(any(HttpServletRequest.class));
        verify(followService).getFollowingCardList(fromUserPayload, toUser.getName());
    }

    @Test
    @Disabled
    void isFollow() {
    }

    @Test
    @Disabled
    void follow() {
    }
}