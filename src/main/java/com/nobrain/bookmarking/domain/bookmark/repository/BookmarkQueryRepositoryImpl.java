package com.nobrain.bookmarking.domain.bookmark.repository;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
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
    public List<Bookmark> findAllByUserId(Long userId) {
        return queryFactory
                .selectFrom(bookmark)
                .join(category).on(bookmark.category.id.eq(category.id))
                .where(category.user.id.eq(userId))
                .fetch();
    }

    @Override
    public List<Bookmark> searchAll(String keyword) {
        return queryFactory
                .selectFrom(bookmark)
                .where(bookmark.title.contains(keyword).or(bookmark.description.contains(keyword)))
                .fetch();
    }
}
