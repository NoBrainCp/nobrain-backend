package com.nobrain.bookmarking.domain.bookmark_tag.service;

import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.bookmark_tag.entity.BookmarkTag;
import com.nobrain.bookmarking.domain.bookmark_tag.repository.BookmarkTagRepository;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import com.nobrain.bookmarking.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkTagService {

    private final BookmarkTagRepository bookmarkTagRepository;
    private final TagService tagService;

    @Transactional
    public void saveTags(Bookmark bookmark, List<String> tags) {
        List<BookmarkTag> bookmarkTags = mapToBookmarkTags(bookmark, tags);
        bookmarkTagRepository.saveAll(bookmarkTags);
    }

    @Transactional
    public void update(Bookmark bookmark, BookmarkRequest.Info requestDto) {
        List<BookmarkTag> bookmarkTags = bookmark.getTags();
        tagService.deleteAllByBookmarkTags(bookmarkTags);

        List<BookmarkTag> savedBookmarkTags = mapToBookmarkTags(bookmark, requestDto.getTags());
        bookmarkTagRepository.saveAll(savedBookmarkTags);
    }

    private List<BookmarkTag> mapToBookmarkTags(Bookmark bookmark, List<String> tags) {
        return tags.stream()
                .map(tagName -> {
                    Tag tag = tagService.findOrCreateTags(tagName);
                    return new BookmarkTag(bookmark, tag);
                })
                .collect(Collectors.toList());
    }
}
