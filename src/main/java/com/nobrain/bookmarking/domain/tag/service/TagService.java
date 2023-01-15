package com.nobrain.bookmarking.domain.tag.service;

import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import com.nobrain.bookmarking.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

    @Transactional
    public void createTags(Bookmark bookmark) {
        for (Tag tag : bookmark.getTags()) {
            tag.addBookmark(bookmark);
        }

        tagRepository.saveAll(bookmark.getTags());
    }

    public Set<Tag> converToTagSet(String tags) {
        Set<Tag> result = new HashSet<>();
        for (String tagName : tags.split(" ")) {
            Tag tag = Tag.builder().name(tagName).build();
            result.add(tag);
        }

        return result;
    }
}
