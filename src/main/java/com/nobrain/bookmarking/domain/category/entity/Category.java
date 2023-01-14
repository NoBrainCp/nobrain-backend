package com.nobrain.bookmarking.domain.category.entity;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Bookmark> bookmarks = new HashSet<>();

    @Builder
    public Category(String name, Set<Bookmark> bookmarks) {
        this.name = name;
        this.bookmarks = bookmarks;
    }
}
