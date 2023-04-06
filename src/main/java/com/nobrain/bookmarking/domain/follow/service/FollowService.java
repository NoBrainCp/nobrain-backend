package com.nobrain.bookmarking.domain.follow.service;

import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.follow.dto.FollowResponse;
import com.nobrain.bookmarking.domain.follow.entity.Follow;
import com.nobrain.bookmarking.domain.follow.repository.FollowQueryRepository;
import com.nobrain.bookmarking.domain.follow.repository.FollowRepository;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final FollowQueryRepository followQueryRepository;
    private final UserRepository userRepository;

    public FollowResponse.FollowCount getFollowCount(String username) {
        Long userId = getUserIdByName(username);
        return FollowResponse.FollowCount.builder()
                .followerCnt(followRepository.countFollowerByUserId(userId))
                .followingCnt(followRepository.countFollowingByUserId(userId))
                .build();
    }

    public List<FollowResponse.Info> getFollowerList(String username) {
        Long userId = getUserIdByName(username);
        return followRepository.findFollowerList(userId).stream()
                .map(f -> FollowResponse.Info.toResponse(f.getFromUser()))
                .collect(Collectors.toList());
    }

    public List<FollowResponse.Info> getFollowingList(String username) {
        Long userId = getUserIdByName(username);
        return followRepository.findFollowingList(userId).stream()
                .map(f -> FollowResponse.Info.toResponse(f.getToUser()))
                .collect(Collectors.toList());
    }

    public List<FollowResponse.FollowCard> getFollowerCardList(UserPayload myPayload, String username) {
        Long userId = getUserIdByName(username);
        Long myId = myPayload.getUserId();
        return followQueryRepository.findAllFollowerCardsByUserId(userId, myId);
    }

    public List<FollowResponse.FollowCard> getFollowingCardList(UserPayload myPayload, String username) {
        Long userId = getUserIdByName(username);
        Long myId = myPayload.getUserId();
        return followQueryRepository.findAllFollowingCardsByUserId(userId, myId);
    }

    public Boolean isFollow(UserPayload myPayload, Long toUserId) {
        Long fromUserId = myPayload.getUserId();
        return followRepository.existsFollowByToUserIdAndFromUserId(toUserId, fromUserId);
    }

    @Transactional
    public void follow(UserPayload myPayload, Long toUserId) {
        Long fromUserId = myPayload.getUserId();
        Follow follow = followRepository.findByFromUserAndToUser(fromUserId, toUserId)
                .orElse(null);

        if (follow == null) {
            User fromUser = getUserById(fromUserId);
            User toUser = getUserById(toUserId);
            follow = Follow.builder()
                    .fromUser(fromUser)
                    .toUser(toUser)
                    .build();

            followRepository.save(follow);
            return;
        }

        unfollow(follow);
    }

    @Transactional
    public void unfollow(Follow follow) {
        followRepository.delete(follow);
    }

    private User getUserById(Long toUserId) {
        return userRepository.findById(toUserId)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(toUserId)));
    }

    private Long getUserIdByName(String username) {
        return userRepository.findByName(username).orElseThrow(() -> new UserNotFoundException(username)).getId();
    }
}
