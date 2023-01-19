package com.nobrain.bookmarking.domain.bookmark.entity;

import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.global.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

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

    @OneToMany(mappedBy = "bookmark")
    private List<Tag> tags = new ArrayList<>();

    @Builder
    public Bookmark(String url, String title, String description, boolean isPublic, boolean isStar, User user, Category category, List<Tag> tags) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.isPublic = isPublic;
        this.isStar = isStar;
        addCategory(category);
        this.tags = tags;
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
}
