package com.nobrain.bookmarking.domain.bookmark_tag.repository;

import com.nobrain.bookmarking.domain.bookmark_tag.dto.projection.BookmarkTagProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nobrain.bookmarking.domain.bookmark.entity.QBookmark.bookmark;
import static com.nobrain.bookmarking.domain.bookmark_tag.entity.QBookmarkTag.bookmarkTag;
import static com.nobrain.bookmarking.domain.category.entity.QCategory.category;
import static com.querydsl.jpa.JPAExpressions.select;

@RequiredArgsConstructor
@Repository
public class BookmarkTagQueryRepositoryImpl implements BookmarkTagQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BookmarkTagProjection.BookmarkAndTag> findAllBookmarkTagsByUserId(Long userId) {
        return queryFactory
                .select(Projections.constructor(BookmarkTagProjection.BookmarkAndTag.class,
                        bookmarkTag.tag,
                        bookmarkTag.bookmark))
                .from(bookmarkTag)
                .join(bookmark).on(bookmark.id.eq(bookmarkTag.bookmark.id))
                .where(bookmarkTag.bookmark.id.in(
                        select(bookmark.id)
                        .from(bookmark)
                        .where(bookmark.category.id.in(
                                select(category.id)
                                .from(category)
                                .where(category.user.id.eq(userId))
                        ))
                ))
                .fetch();
    }

    @Override
    public List<BookmarkTagProjection.BookmarkAndTag> findBookmarkTagsByUserIdAndTagList(Long userId, List<Long> tagIds) {
        return queryFactory
                .select(Projections.constructor(BookmarkTagProjection.BookmarkAndTag.class,
                        bookmarkTag.tag,
                        bookmarkTag.bookmark))
                .from(bookmarkTag)
                .join(bookmark).on(bookmark.id.eq(bookmarkTag.bookmark.id))
                .where(bookmarkTag.tag.id.in(tagIds)
                        .and(bookmarkTag.bookmark.id.in(
                                select(bookmark.id)
                                .from(bookmark)
                                .where(bookmark.category.id.in(
                                        select(category.id)
                                        .from(category)
                                        .where(category.user.id.eq(userId))
                                ))
                        )
                ))
                .fetch();
    }
}
