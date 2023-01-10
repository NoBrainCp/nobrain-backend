package com.nobrain.bookmarking.domain.bookmark;

import com.nobrain.bookmarking.domain.category.Category;
import com.nobrain.bookmarking.domain.tag.Tag;
import com.nobrain.bookmarking.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Bookmark {

    @Id @GeneratedValue
    @Column(name = "bookmark_id")
    private Long id;

    private String url;
    private String title;

    @Lob
    private String description;
    private LocalDateTime createdAt;
    private boolean isPublic;
    private boolean isStar;
    private String addColumn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "bookmark")
    private Set<Tag> tags = new HashSet<>();

    @Builder
    public Bookmark(String url, String title, String description, LocalDateTime createdAt, boolean isPublic, boolean isStar, User user, Category category, Set<Tag> tags) {
        this.url = url;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.isPublic = isPublic;
        this.isStar = isStar;
        this.user = user;
        this.category = category;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookmark bookmark = (Bookmark) o;
        return Objects.equals(getUrl(), bookmark.getUrl()) && Objects.equals(getUser(), bookmark.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), getUser());
    }

    /**
     * 연관관계 편의 메서드
     */
    public void addUser(User user) {
        this.user = user;
        user.getBookmarks().add(this);
    }

    public void addCategory(Category category) {
        this.category = category;
        category.getBookmarks().add(this);
    }
}
