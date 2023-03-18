package com.nobrain.bookmarking.domain.follow.repository;

import com.nobrain.bookmarking.domain.follow.entity.Follow;
import com.nobrain.bookmarking.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByToUserOrFromUser(User toUser, User fromUser);

    @Query("select f from Follow f " +
            "where f.fromUser.id = :fromUserId " +
            "and f.toUser.id = :toUserId ")
    Optional<Follow> findByFromUserAndToUser(@Param("fromUserId") Long fromUserId, @Param("toUserId") Long toUserId);

    @Query("select count(f) from Follow f where f.toUser.id = :userId")
    Integer countFollowerByUserId(@Param("userId") Long userId);

    @Query("select count(f) from Follow f where f.fromUser.id = :userId")
    Integer countFollowingByUserId(@Param("userId") Long userId);

    @Query("select f from Follow f where f.toUser.id = :userId")
    List<Follow> findFollowerList(@Param("userId") Long userId);

    @Query("select f from Follow f where f.fromUser.id = :userId")
    List<Follow> findFollowingList(@Param("userId") Long userId);

    Boolean existsFollowByToUserIdAndFromUserId(Long toUserId, Long fromUserId);
}
