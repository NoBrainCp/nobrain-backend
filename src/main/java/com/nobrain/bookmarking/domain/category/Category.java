package com.nobrain.bookmarking.domain.category;

import com.nobrain.bookmarking.domain.bookmark.Bookmark;
import lombok.Getter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private Set<Bookmark> bookmarks = new HashSet<>();
}
