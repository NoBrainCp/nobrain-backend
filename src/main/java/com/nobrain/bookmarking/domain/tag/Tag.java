package com.nobrain.bookmarking.domain.tag;

import com.nobrain.bookmarking.domain.bookmark.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookmark_id")
    private Bookmark bookmark;

    /**
     * 연관관계 편의 메서드
     */
    public void addBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
        bookmark.getTags().add(this);
    }
}
