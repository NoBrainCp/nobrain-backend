package com.nobrain.bookmarking.domain.bookmark.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookmark is a Querydsl query type for Bookmark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookmark extends EntityPathBase<Bookmark> {

    private static final long serialVersionUID = -2123002197L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookmark bookmark = new QBookmark("bookmark");

    public final com.nobrain.bookmarking.global.entity.QBaseTimeEntity _super = new com.nobrain.bookmarking.global.entity.QBaseTimeEntity(this);

    public final com.nobrain.bookmarking.domain.category.entity.QCategory category;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isPublic = createBoolean("isPublic");

    public final BooleanPath isStar = createBoolean("isStar");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final ListPath<com.nobrain.bookmarking.domain.tag.entity.Tag, com.nobrain.bookmarking.domain.tag.entity.QTag> tags = this.<com.nobrain.bookmarking.domain.tag.entity.Tag, com.nobrain.bookmarking.domain.tag.entity.QTag>createList("tags", com.nobrain.bookmarking.domain.tag.entity.Tag.class, com.nobrain.bookmarking.domain.tag.entity.QTag.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final StringPath url = createString("url");

    public QBookmark(String variable) {
        this(Bookmark.class, forVariable(variable), INITS);
    }

    public QBookmark(Path<? extends Bookmark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookmark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookmark(PathMetadata metadata, PathInits inits) {
        this(Bookmark.class, metadata, inits);
    }

    public QBookmark(Class<? extends Bookmark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new com.nobrain.bookmarking.domain.category.entity.QCategory(forProperty("category"), inits.get("category")) : null;
    }

}

