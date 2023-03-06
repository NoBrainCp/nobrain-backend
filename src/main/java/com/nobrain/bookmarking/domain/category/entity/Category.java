package com.nobrain.bookmarking.domain.category.entity;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.category.dto.CategoryRequest;
import com.nobrain.bookmarking.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    @Lob
    private String description;
    private boolean isPublic;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "category", cascade = ALL)
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder
    public Category(String name, String description, boolean isPublic, User user, List<Bookmark> bookmarks) {
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        addUser(user);
        this.bookmarks = bookmarks;
    }

    public void update(CategoryRequest.Info dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.isPublic = dto.isPublic();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(getName(), category.getName()) && Objects.equals(getUser(), category.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUser());
    }

    /**
     * 연관관계 메서드
     */
    public void addUser(User user) {
        this.user = user;
        user.getCategories().add(this);
    }
}
