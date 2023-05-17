package com.nobrain.bookmarking.domain.follow.service;

import com.nobrain.bookmarking.ServiceTest;
import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.follow.dto.FollowResponse;
import com.nobrain.bookmarking.domain.follow.entity.Follow;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.nobrain.bookmarking.Constants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class FollowServiceTest extends ServiceTest {

    @Autowired
    private FollowService followService;

    private User fromUser;
    private User toUser;
    private UserPayload fromUserPayload;
    private UserPayload toUserPayload;
    private Follow follow;

    @BeforeEach
    void setUp() {
        fromUser = User.builder()
                .id(USER_ID)
                .name(USERNAME)
                .email(EMAIL)
                .password(PASSWORD)
                .profileImage(PROFILE_IMG)
                .categories(new ArrayList<>())
                .build();

        toUser = User.builder()
                .id(TO_USER_ID)
                .name(TO_USERNAME)
                .email(TO_USER_EMAIL)
                .password(TO_USER_PASSWORD)
                .profileImage(TO_USER_PROFILE_IMG)
                .categories(new ArrayList<>())
                .build();

        fromUserPayload = UserPayload.builder()
                .userId(USER_ID)
                .username(USERNAME)
                .build();

        toUserPayload = UserPayload.builder()
                .userId(TO_USER_ID)
                .username(TO_USERNAME)
                .build();

        follow = Follow.builder()
                .id(FOLLOW_ID)
                .toUser(toUser)
                .fromUser(fromUser)
                .build();
    }

    @Test
    @DisplayName("팔로워, 팔로잉 수 조회 - 성공")
    void getFollowCount() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(fromUser));
        given(follows.countFollowerByUserId(anyLong()))
                .willReturn(FOLLOWER_COUNT);
        given(follows.countFollowingByUserId(anyLong()))
                .willReturn(FOLLOWING_COUNT);

        // when
        FollowResponse.FollowCount actual = followService.getFollowCount(fromUser.getName());

        // then
        FollowResponse.FollowCount expected = new FollowResponse.FollowCount(FOLLOWER_COUNT, FOLLOWING_COUNT);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

        verify(users).findByName(fromUser.getName());
        verify(follows).countFollowerByUserId(fromUser.getId());
        verify(follows).countFollowingByUserId(fromUser.getId());
    }

    @Test
    @DisplayName("팔로워, 팔로잉 수 조회 - 존재하지 않는 유저 실패")
    void getFollowCount_not_exists_user() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> followService.getFollowCount(fromUser.getName()))
                .isInstanceOf(UserNotFoundException.class);

        verify(users).findByName(fromUser.getName());
    }


    @Test
    @DisplayName("팔로워 리스트 조회 - 성공")
    void getFollowerList() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(toUser));
        given(follows.findFollowerList(anyLong()))
                .willReturn(List.of(follow));

        // when
        List<FollowResponse.Info> actual = followService.getFollowerList(toUser.getName());

        // then
        List<FollowResponse.Info> expected = Stream.of(follow)
                .map(f -> FollowResponse.Info.toResponse(f.getFromUser()))
                .collect(Collectors.toList());

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

        verify(users).findByName(toUser.getName());
        verify(follows).findFollowerList(toUser.getId());
    }

    @Test
    @DisplayName("팔로잉 리스트 조회 - 성공")
    void getFollowingList() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(fromUser));
        given(follows.findFollowingList(anyLong()))
                .willReturn(List.of(follow));

        // when
        List<FollowResponse.Info> actual = followService.getFollowingList(fromUser.getName());

        // then
        List<FollowResponse.Info> expected = Stream.of(follow)
                .map(f -> FollowResponse.Info.toResponse(f.getToUser()))
                .collect(Collectors.toList());

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

        verify(users).findByName(fromUser.getName());
        verify(follows).findFollowingList(fromUser.getId());
    }

    @Test
    @DisplayName("팔로우 유저 카드 리스트 조회 - 성공")
    void getFollowerCardList() {
        // given
        FollowResponse.FollowCard followerCard = FollowResponse.FollowCard.builder()
                .userId(fromUser.getId())
                .username(fromUser.getName())
                .profileImage(fromUser.getProfileImage())
                .bookmarkCount(FROM_USER_BOOKMARK_COUNT)
                .followerCount((long) FOLLOWER_COUNT)
                .followingCount((long) FOLLOWING_COUNT)
                .isFollow(IS_FOLLOW)
                .build();

        given(users.findByName(anyString()))
                .willReturn(Optional.of(toUser));
        given(followQueryRepository.findAllFollowerCardsByUserId(anyLong(), anyLong()))
                .willReturn(List.of(followerCard));

        // when
        List<FollowResponse.FollowCard> actual = followService.getFollowerCardList(fromUserPayload, toUser.getName());

        // then
        List<FollowResponse.FollowCard> expected = List.of(followerCard);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

        verify(users).findByName(toUser.getName());
        verify(followQueryRepository).findAllFollowerCardsByUserId(toUser.getId(), fromUserPayload.getUserId());
    }

    @Test
    @DisplayName("팔로잉 유저 카드 리스트 조회")
    void getFollowingCardList() {
        // given
        FollowResponse.FollowCard followingCard = FollowResponse.FollowCard.builder()
                .userId(toUser.getId())
                .username(toUser.getName())
                .profileImage(toUser.getProfileImage())
                .bookmarkCount(FROM_USER_BOOKMARK_COUNT)
                .followerCount((long) FOLLOWER_COUNT)
                .followingCount((long) FOLLOWING_COUNT)
                .isFollow(IS_FOLLOW)
                .build();

        given(users.findByName(anyString()))
                .willReturn(Optional.of(fromUser));
        given(followQueryRepository.findAllFollowingCardsByUserId(anyLong(), anyLong()))
                .willReturn(List.of(followingCard));

        // when
        List<FollowResponse.FollowCard> actual = followService.getFollowingCardList(toUserPayload, fromUser.getName());

        // then
        List<FollowResponse.FollowCard> expected = List.of(followingCard);

        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

        verify(users).findByName(fromUser.getName());
        verify(followQueryRepository).findAllFollowingCardsByUserId(fromUser.getId(), toUserPayload.getUserId());
    }

    @Test
    @DisplayName("팔로우 상태(follow/unfollow) 확인 - 성공")
    void isFollow() {
        // given
        given(follows.existsFollowByToUserIdAndFromUserId(anyLong(), anyLong()))
                .willReturn(IS_FOLLOW);

        // when, then
        assertThat(followService.isFollow(fromUserPayload, toUser.getId())).isTrue();

        verify(follows).existsFollowByToUserIdAndFromUserId(toUser.getId(), fromUserPayload.getUserId());
    }

    @Test
    @DisplayName("기존에 팔로우가 되어있지 않으면 팔로우 - 성공")
    void follow() {
        // given
        given(follows.findByFromUserAndToUser(anyLong(), anyLong()))
                .willReturn(Optional.empty());
        given(users.findById(anyLong()))
                .willReturn(Optional.of(fromUser))
                .willReturn(Optional.of(toUser));

        // when, then
        assertThatCode(() -> followService.follow(fromUserPayload, toUser.getId()))
                .doesNotThrowAnyException();

        verify(follows).findByFromUserAndToUser(fromUserPayload.getUserId(), toUser.getId());
        verify(users).findById(fromUserPayload.getUserId());
        verify(users).findById(toUser.getId());
    }

    @Test
    @DisplayName("기존에 팔로우가 되어있으면 언팔로우 - 성공")
    void follow_unfollow() {
        // given
        given(follows.findByFromUserAndToUser(anyLong(), anyLong()))
                .willReturn(Optional.of(follow));

        // when, then
        assertThatCode(() -> followService.follow(fromUserPayload, toUser.getId()))
                .doesNotThrowAnyException();

        verify(follows).findByFromUserAndToUser(fromUserPayload.getUserId(), toUser.getId());
    }

    @Test
    @DisplayName("팔로우 취소 - 성공")
    void unfollow() {
        assertThatCode(() -> followService.unfollow(follow))
                .doesNotThrowAnyException();
    }
}