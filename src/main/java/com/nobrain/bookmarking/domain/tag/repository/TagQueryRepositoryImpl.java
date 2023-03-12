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

@RequiredArgsConstructor
@Repository
public class TagQueryRepositoryImpl implements TagQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Tag> findAllByUser(String username) {
        return queryFactory
                .selectDistinct(tag)
                .from(tag)
                .join(bookmarkTag).on(tag.id.eq(bookmarkTag.tag.id))
                .join(bookmark).on(bookmarkTag.bookmark.id.eq(bookmark.id))
                .join(category).on(bookmark.category.id.eq(category.id))
                .where(category.user.name.eq(username))
                .fetch();
    }

    @Override
    public List<Tag> findTagsByBookmarkId(String username, Long bookmarkId) {
        return queryFactory
                .select(tag)
                .from(tag)
                .join(bookmarkTag).on(tag.id.eq(bookmarkTag.tag.id))
                .where(bookmarkTag.bookmark.id.eq(bookmarkId))
                .fetch();
    }
}
