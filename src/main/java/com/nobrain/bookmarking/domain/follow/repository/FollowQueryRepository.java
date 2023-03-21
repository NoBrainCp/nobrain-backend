package com.nobrain.bookmarking.domain.follow.repository;

import com.nobrain.bookmarking.domain.follow.dto.FollowResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowQueryRepository {
    List<FollowResponse.FollowCard> findAllFollowerCardsByUsername(Long userId);
}
