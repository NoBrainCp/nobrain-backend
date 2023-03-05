package com.nobrain.bookmarking.domain.tag.repository;

import com.nobrain.bookmarking.domain.tag.entity.Tag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nobrain.bookmarking.domain.bookmark.entity.QBookmark.bookmark;
import static com.nobrain.bookmarking.domain.bookmark_tag.entity.QBookmarkTag.bookmarkTag;
import static com.nobrain.bookmarking.domain.category.entity.QCategory.category;
import static com.nobrain.bookmarking.domain.tag.entity.QTag.tag;
import static com.querydsl.jpa.JPAExpressions.select;
import static com.querydsl.jpa.JPAExpressions.selectDistinct;

@RequiredArgsConstructor
@Repository
public class TagQueryRepositoryImpl implements TagQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tag> findAllByUser(Long userId) {
        return queryFactory
                .selectFrom(tag)
                .where(tag.id.in(
                        selectDistinct(bookmarkTag.tag.id)
                        .from(bookmarkTag)
                        .where(bookmarkTag.bookmark.id.in(
                                select(bookmark.id)
                                .from(bookmark)
                                .where(bookmark.category.id.in(
                                        select(category.id)
                                        .from(category)
                                        .where(category.user.id.eq(userId))
                                ))
                        ))
                ))
                .fetch();
    }
}
