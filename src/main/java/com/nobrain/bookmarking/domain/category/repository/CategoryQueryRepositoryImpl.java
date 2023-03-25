package com.nobrain.bookmarking.domain.category.repository;

import com.nobrain.bookmarking.domain.category.dto.CategoryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nobrain.bookmarking.domain.bookmark.entity.QBookmark.bookmark;
import static com.nobrain.bookmarking.domain.category.entity.QCategory.category;

@RequiredArgsConstructor
@Repository
public class CategoryQueryRepositoryImpl implements CategoryQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CategoryResponse.Info> findAllCategoryInfoWithCount(String username) {
        return queryFactory.select(Projections.constructor(CategoryResponse.Info.class,
                        category.id,
                        category.name,
                        category.description,
                        category.isPublic,
                        bookmark.id.count()))
                .from(category)
                .leftJoin(bookmark).on(bookmark.category.id.eq(category.id))
                .where(category.user.name.eq(username))
                .groupBy(category.name)
                .orderBy(bookmark.id.count().desc())
                .fetch();
    }

    @Override
    public CategoryResponse.Info findCategoryByBookmarkId(Long bookmarkId) {
        return queryFactory.select(Projections.constructor(CategoryResponse.Info.class,
                        category.id,
                        category.name,
                        category.description,
                        category.isPublic,
                        bookmark.id.count()))
                .from(category)
                .join(bookmark).on(category.id.eq(bookmark.category.id))
                .where(bookmark.id.eq(bookmarkId))
                .fetchOne();
    }

    @Override
    public Boolean findCategoryIsPublic(Long userId, String categoryName) {
        return queryFactory
                .select(category.isPublic)
                .from(category)
                .where(category.user.id.eq(userId).and(category.name.eq(categoryName)))
                .fetchOne();
    }
}
