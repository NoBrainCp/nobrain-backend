package com.nobrain.bookmarking.domain.bookmark_tag.service;

import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.bookmark_tag.dto.BookmarkTagResponse;
import com.nobrain.bookmarking.domain.bookmark_tag.dto.projection.BookmarkTagProjection;
import com.nobrain.bookmarking.domain.bookmark_tag.entity.BookmarkTag;
import com.nobrain.bookmarking.domain.bookmark_tag.repository.BookmarkTagQueryRepository;
import com.nobrain.bookmarking.domain.bookmark_tag.repository.BookmarkTagRepository;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import com.nobrain.bookmarking.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkTagService {

    private final BookmarkTagQueryRepository bookmarkTagQueryRepository;
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

    public List<BookmarkTagResponse.Info> getAllBookmarkTags(String username) {
        List<BookmarkTagProjection.BookmarkAndTag> bookmarkTagsByUserId = bookmarkTagQueryRepository.findAllBookmarkTagsByUserId(username);
        return mapToBookmarkTagResponse(bookmarkTagsByUserId);
    }

    public List<BookmarkTagResponse.Info> getBookmarkTagsByTagList(String username, List<Long> tagIds) {
        List<BookmarkTagProjection.BookmarkAndTag> bookmarkTagsByUserIdAndTagList = bookmarkTagQueryRepository.findBookmarkTagsByUserIdAndTagList(username, tagIds);
        return mapToBookmarkTagResponse(bookmarkTagsByUserIdAndTagList);
    }

    private List<BookmarkTag> mapToBookmarkTags(Bookmark bookmark, List<String> tags) {
        return tags.stream()
                .map(tagName -> {
                    Tag tag = tagService.findOrCreateTags(tagName);
                    return new BookmarkTag(bookmark, tag);
                })
                .collect(Collectors.toList());
    }

    private static List<BookmarkTagResponse.Info> mapToBookmarkTagResponse(List<BookmarkTagProjection.BookmarkAndTag> bookmarkByUserIdAndTags) {
        Map<Tag, List<Bookmark>> bookmarkTagMap = new HashMap<>();
        for (BookmarkTagProjection.BookmarkAndTag bookmarkByUserIdAndTag : bookmarkByUserIdAndTags) {
            bookmarkTagMap.put(bookmarkByUserIdAndTag.getTag(), bookmarkTagMap.getOrDefault(bookmarkByUserIdAndTag.getTag(), new ArrayList<>()));
            bookmarkTagMap.get(bookmarkByUserIdAndTag.getTag()).add(bookmarkByUserIdAndTag.getBookmark());
        }

        List<BookmarkTagResponse.Info> bookmarkTagResponses = new ArrayList<>();
        for (Tag key : bookmarkTagMap.keySet()) {
            bookmarkTagResponses.add(new BookmarkTagResponse.Info(key, bookmarkTagMap.get(key)));
        }
        return bookmarkTagResponses;
    }
}
