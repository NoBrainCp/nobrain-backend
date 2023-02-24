package com.nobrain.bookmarking.domain.category.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = -1978454469L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCategory category = new QCategory("category");

    public final ListPath<com.nobrain.bookmarking.domain.bookmark.entity.Bookmark, com.nobrain.bookmarking.domain.bookmark.entity.QBookmark> bookmarks = this.<com.nobrain.bookmarking.domain.bookmark.entity.Bookmark, com.nobrain.bookmarking.domain.bookmark.entity.QBookmark>createList("bookmarks", com.nobrain.bookmarking.domain.bookmark.entity.Bookmark.class, com.nobrain.bookmarking.domain.bookmark.entity.QBookmark.class, PathInits.DIRECT2);

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isPublic = createBoolean("isPublic");

    public final StringPath name = createString("name");

    public final com.nobrain.bookmarking.domain.user.entity.QUser user;

    public QCategory(String variable) {
        this(Category.class, forVariable(variable), INITS);
    }

    public QCategory(Path<? extends Category> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCategory(PathMetadata metadata, PathInits inits) {
        this(Category.class, metadata, inits);
    }

    public QCategory(Class<? extends Category> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.nobrain.bookmarking.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

