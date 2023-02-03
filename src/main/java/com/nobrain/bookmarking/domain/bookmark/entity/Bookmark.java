package com.nobrain.bookmarking.domain.bookmark.entity;

import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private boolean isStar;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "bookmark", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    @Builder
    public Bookmark(String url, String title, String description, boolean isPublic, boolean isStar, User user, Category category, List<Tag> tags) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.isPublic = isPublic;
        this.isStar = isStar;
        addCategory(category);
        addTags(tags);
    }

    public void update(BookmarkRequest.Info requestDto, Category category) {
        this.url = requestDto.getUrl();
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
        this.isPublic = requestDto.isPublic();
        this.isStar = requestDto.isStar();
        this.category = category;
        this.tags = requestDto.getTags().stream()
                .map(tag -> Tag.builder().name(tag).bookmark(this).build())
                .collect(Collectors.toList());
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

    /**
     * 연관관계 메서드
     */
    public void addCategory(Category category) {
        this.category = category;
        category.getBookmarks().add(this);
    }

    public void addTags(List<Tag> tags) {
        this.tags = tags;
        tags.forEach(tag -> tag.setBookmark(this));
    }
}
