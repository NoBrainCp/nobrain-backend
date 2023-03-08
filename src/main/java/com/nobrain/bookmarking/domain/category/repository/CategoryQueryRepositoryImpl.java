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
    public List<CategoryResponse.Info> findAllCategoryInfoWithCount(Long userId) {
        return queryFactory.select(Projections.constructor(CategoryResponse.Info.class,
                        category.id,
                        category.name,
                        category.description,
                        category.isPublic,
                        category.count()))
                .from(category)
                .join(bookmark).on(bookmark.category.id.eq(category.id)
                        .and(category.user.id.eq(userId)))
                .groupBy(category.name)
                .fetch();
    }
}
