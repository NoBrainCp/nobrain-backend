package com.nobrain.bookmarking.domain.category.entity;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "category")
    private List<Bookmark> bookmarks = new ArrayList<>();

    @Builder
    public Category(String name, User user, List<Bookmark> bookmarks) {
        this.name = name;
        addUser(user);
        this.bookmarks = bookmarks;
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
        user.getCategory().add(this);
    }
}
