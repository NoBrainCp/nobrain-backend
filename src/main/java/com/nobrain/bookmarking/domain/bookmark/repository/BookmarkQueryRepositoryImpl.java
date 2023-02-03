package com.nobrain.bookmarking.domain.bookmark.repository;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nobrain.bookmarking.domain.bookmark.entity.QBookmark.bookmark;
import static com.nobrain.bookmarking.domain.category.entity.QCategory.category;

@Repository
@RequiredArgsConstructor
public class BookmarkQueryRepositoryImpl implements BookmarkQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Bookmark> findAllByUser(long userId) {
        return queryFactory.select(bookmark)
                .from(bookmark)
                .where(bookmark.category.id.in(
                        JPAExpressions.select(category.id)
                                .from(category)
                                .where(category.user.id.eq(userId))
                ))
                .fetch();
    }
}
