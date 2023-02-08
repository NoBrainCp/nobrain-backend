package com.nobrain.bookmarking.domain.tag.entity;

import com.nobrain.bookmarking.domain.bookmark_tag.entity.BookmarkTag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tag {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "tag", cascade = ALL)
    List<BookmarkTag> bookmarks = new ArrayList<>();

    @Builder
    public Tag(String name) {
        this.name = name;
    }
}
