package com.nobrain.bookmarking.domain.tag.service;

import com.nobrain.bookmarking.domain.bookmark_tag.entity.BookmarkTag;
import com.nobrain.bookmarking.domain.tag.dto.TagResponse;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import com.nobrain.bookmarking.domain.tag.repository.TagQueryRepository;
import com.nobrain.bookmarking.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TagService {

    private final TagQueryRepository tagQueryRepository;
    private final TagRepository tagRepository;

    @Transactional
    public Tag findOrCreateTags(String tagName) {
        Tag tag = tagRepository.findByName(tagName)
                .orElseGet(() -> Tag.builder()
                        .name(tagName)
                        .build());

        return tagRepository.save(tag);
    }

    @Transactional
    public void deleteAllByBookmarkTags(List<BookmarkTag> bookmarkTags) {
        List<Tag> removedTags = mapToTags(bookmarkTags);
        tagRepository.deleteAll(removedTags);
    }

    public List<TagResponse.Info> getAllTagsOfUser(String username) {
        return tagQueryRepository.findAllByUser(username).stream()
                .map(tag -> TagResponse.Info.builder()
                        .tagId(tag.getId())
                        .tagName(tag.getName())
                        .build()
                ).collect(Collectors.toList());
    }

    public List<TagResponse.Info> getTagsOfUserByBookmarkId(String username, Long bookmarkId) {
        return tagQueryRepository.findTagsByBookmarkId(username, bookmarkId).stream()
                .map(tag -> TagResponse.Info.builder()
                        .tagId(tag.getId())
                        .tagName(tag.getName())
                        .build()
                ).collect(Collectors.toList());
    }

    private List<Tag> mapToTags(List<BookmarkTag> bookmarkTags) {
        return bookmarkTags.stream()
                .map(BookmarkTag::getTag)
                .collect(Collectors.toList());
    }
}
