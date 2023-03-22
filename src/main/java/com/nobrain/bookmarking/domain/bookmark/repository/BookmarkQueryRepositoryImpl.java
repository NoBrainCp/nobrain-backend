package com.nobrain.bookmarking.domain.bookmark.repository;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nobrain.bookmarking.domain.bookmark.entity.QBookmark.bookmark;
import static com.nobrain.bookmarking.domain.bookmark.search.SearchCondition.FOLLOW;
import static com.nobrain.bookmarking.domain.bookmark.search.SearchCondition.MY;
import static com.nobrain.bookmarking.domain.category.entity.QCategory.category;
import static com.nobrain.bookmarking.domain.follow.entity.QFollow.follow;
import static com.nobrain.bookmarking.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class BookmarkQueryRepositoryImpl implements BookmarkQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Bookmark> findAllByUserId(Long userId) {
        return queryFactory
                .selectFrom(bookmark)
                .join(category).on(bookmark.category.id.eq(category.id))
                .where(category.user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Bookmark> findAllStarredBookmarksByUserId(Long userId, Boolean isMe) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (!isMe) {
            booleanBuilder.and(bookmark.isPublic.eq(true));
        }

        return queryFactory.selectFrom(bookmark)
                .join(category).on(bookmark.category.id.eq(category.id))
                .join(user).on(category.user.id.eq(user.id))
                .where(bookmark.isStarred.eq(true).and(user.id.eq(userId)).and(booleanBuilder))
                .fetch();
    }

    @Override
    public List<Bookmark> searchBookmarksByCondition(String keyword, String condition, Long userId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(bookmark.title.containsIgnoreCase(keyword).or(bookmark.description.containsIgnoreCase(keyword)));

        JPAQuery<Bookmark> query = queryFactory.selectFrom(bookmark);

        if (condition.equals(MY.getCondition())) {
            query.join(category).on(bookmark.category.id.eq(category.id));
            booleanBuilder.and(category.user.id.eq(userId));
        } else if (condition.equals(FOLLOW.getCondition())) {
            query.join(category).on(bookmark.category.id.eq(category.id))
                    .join(user).on(category.user.id.eq(user.id))
                    .join(follow).on(user.id.eq(follow.toUser.id));
            booleanBuilder.and(follow.fromUser.id.eq(userId));
        }

        return query
                .where(booleanBuilder)
                .fetch();
    }
}
