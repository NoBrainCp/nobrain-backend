package com.nobrain.bookmarking.domain.follow.repository;

import com.nobrain.bookmarking.domain.follow.dto.FollowResponse;
import com.nobrain.bookmarking.domain.follow.entity.QFollow;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nobrain.bookmarking.domain.bookmark.entity.QBookmark.bookmark;
import static com.nobrain.bookmarking.domain.category.entity.QCategory.category;
import static com.nobrain.bookmarking.domain.follow.entity.QFollow.follow;
import static com.nobrain.bookmarking.domain.user.entity.QUser.user;
import static com.querydsl.jpa.JPAExpressions.selectOne;

@Repository
@RequiredArgsConstructor
public class FollowQueryRepositoryImpl implements FollowQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FollowResponse.FollowCard> findAllFollowerCardsByUserId(Long userId, Long myId) {
        QFollow f1 = new QFollow("f1");
        QFollow f2 = new QFollow("f2");
        Expression<Boolean> isFollow = isFollowingMe(myId);

        return queryFactory
                .select(Projections.constructor(FollowResponse.FollowCard.class,
                        user.id,
                        user.name,
                        user.profileImage,
                        bookmark.id.countDistinct(),
                        f1.fromUser.id.countDistinct(),
                        f2.toUser.id.countDistinct(),
                        isFollow))
                .from(follow)
                .join(user).on(follow.fromUser.id.eq(user.id))
                .leftJoin(category).on(follow.fromUser.id.eq(category.user.id))
                .leftJoin(bookmark).on(category.id.eq(bookmark.category.id))
                .leftJoin(f1).on(user.id.eq(f1.toUser.id))
                .leftJoin(f2).on(user.id.eq(f2.fromUser.id))
                .where(follow.toUser.id.eq(userId))
                .groupBy(user.id)
                .fetch();
    }

    @Override
    public List<FollowResponse.FollowCard> findAllFollowingCardsByUserId(Long userId, Long myId) {
        QFollow f1 = new QFollow("f1");
        QFollow f2 = new QFollow("f2");
        Expression<Boolean> isFollow = isFollowingMe(myId);

        return queryFactory
                .select(Projections.constructor(FollowResponse.FollowCard.class,
                        user.id,
                        user.name,
                        user.profileImage,
                        bookmark.id.countDistinct(),
                        f1.fromUser.id.countDistinct(),
                        f2.toUser.id.countDistinct(),
                        isFollow))
                .from(follow)
                .join(user).on(follow.toUser.id.eq(user.id))
                .leftJoin(category).on(follow.toUser.id.eq(category.user.id))
                .leftJoin(bookmark).on(category.id.eq(bookmark.category.id))
                .leftJoin(f1).on(user.id.eq(f1.toUser.id))
                .leftJoin(f2).on(user.id.eq(f2.fromUser.id))
                .where(follow.fromUser.id.eq(userId))
                .groupBy(user.id)
                .fetch();
    }

    private Expression<Boolean> isFollowingMe(Long myId) {
        return selectOne()
                .from(follow)
                .where(follow.fromUser.id.eq(myId), follow.toUser.id.eq(user.id)).exists();
    }
}
