package com.nobrain.bookmarking.domain.follow.service;

import com.nobrain.bookmarking.domain.follow.entity.Follow;
import com.nobrain.bookmarking.domain.follow.repository.FollowRepository;
import com.nobrain.bookmarking.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void follow(Long toUserId) {
        Long fromUserId = jwtTokenProvider.getId();
        Follow follow = followRepository.findByFromUserAndToUser(fromUserId, toUserId).orElse(null);

        if (follow == null) {
            follow = Follow.builder()
                    .fromUser(fromUserId)
                    .toUser(toUserId)
                    .build();

            followRepository.save(follow);
            return;
        }

        unFollow(follow);
    }

    @Transactional
    public void unFollow(Follow follow) {
        followRepository.delete(follow);
    }
}
