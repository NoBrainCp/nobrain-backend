package com.nobrain.bookmarking.domain.bookmark_tag.entity;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class BookmarkTag {

    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "bookmark_tag_id")
    private Long id;

    @ManyToOne(cascade = PERSIST)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    @ManyToOne(cascade = {PERSIST, REMOVE})
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public BookmarkTag(Bookmark bookmark, Tag tag) {
        this.bookmark = bookmark;
        this.tag = tag;
    }

    public void update(Bookmark bookmark, Tag tag) {
        this.bookmark = bookmark;
        this.tag = tag;
    }
}
