package com.nobrain.bookmarking.domain.bookmark.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkResponse;
import com.nobrain.bookmarking.domain.bookmark_tag.entity.BookmarkTag;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.global.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Bookmark extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "bookmark_id")
    private Long id;

    private String url;
    private String title;

    @Lob
    private String description;
    private boolean isPublic;
    private boolean isStarred;
    private String metaImage;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "bookmark", cascade = CascadeType.ALL)
    private List<BookmarkTag> tags = new ArrayList<>();

    @Builder
    public Bookmark(String url, String title, String description, boolean isPublic, boolean isStarred, String metaImage, Category category) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.isPublic = isPublic;
        this.isStarred = isStarred;
        this.metaImage = metaImage;
        addCategory(category);
    }

    public void update(BookmarkRequest.Info requestDto, String metaImage, Category category) {
        this.url = requestDto.getUrl();
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.isPublic = requestDto.isPublic();
        this.isStarred = requestDto.isStarred();
        this.category = category;
        this.metaImage = metaImage;
    }

    public void changeStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public void changePublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookmark bookmark = (Bookmark) o;
        return Objects.equals(getUrl(), bookmark.getUrl()) && Objects.equals(getCategory(), bookmark.getCategory());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), getCategory());
    }

    public BookmarkResponse.Info toInfoDto() {
        return BookmarkResponse.Info.builder()
                .id(this.getId())
                .url(this.getUrl())
                .title(this.getTitle())
                .description(this.getDescription())
                .isPublic(this.isPublic())
                .isStarred(this.isStarred())
                .image(this.getMetaImage())
                .createdAt(this.getCreatedAt().toLocalDate())
                .build();
    }

    /**
     * 연관관계 메서드
     */
    public void addCategory(Category category) {
        this.category = category;
        category.getBookmarks().add(this);
    }
}
