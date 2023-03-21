package com.nobrain.bookmarking.domain.follow.repository;

import com.nobrain.bookmarking.domain.follow.dto.FollowResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nobrain.bookmarking.domain.bookmark.entity.QBookmark.bookmark;
import static com.nobrain.bookmarking.domain.category.entity.QCategory.category;
import static com.nobrain.bookmarking.domain.follow.entity.QFollow.follow;
import static com.nobrain.bookmarking.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class FollowQueryRepositoryImpl implements FollowQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FollowResponse.FollowCard> findAllFollowerCardsByUsername(Long userId) {
        return queryFactory
                .select(Projections.constructor(FollowResponse.FollowCard.class,
                        user.id,
                        user.name,
                        bookmark.id.countDistinct(),
                        follow.fromUser.id.countDistinct(),
                        follow.toUser.id.countDistinct()))
                .from(follow)
                .join(user).on(follow.fromUser.id.eq(user.id))
                .leftJoin(category).on(follow.fromUser.id.eq(category.user.id))
                .leftJoin(bookmark).on(category.id.eq(bookmark.category.id))
                .leftJoin(follow).on(user.id.eq(follow.toUser.id))
                .leftJoin(follow).on(user.id.eq(follow.fromUser.id))
                .where(follow.toUser.id.eq(userId))
                .groupBy(user.id)
                .fetch();
    }
}
