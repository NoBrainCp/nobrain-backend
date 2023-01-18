package com.nobrain.bookmarking.domain.follow.repository;

import com.nobrain.bookmarking.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Follow.PK> {

    Optional<Follow> findByFromUserAndToUser(Long fromUserId, Long toUserId);

    Integer countByToUser(Long userId); // follower
    Integer countByFromUser(Long userId); // following
}
