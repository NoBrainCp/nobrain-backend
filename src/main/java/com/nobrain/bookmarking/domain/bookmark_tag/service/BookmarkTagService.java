package com.nobrain.bookmarking.domain.bookmark_tag.service;

import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkResponse;
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

import java.util.*;
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

        bookmarkTagRepository.deleteAllInBatch(bookmarkTags);
        tagService.deleteAllByBookmarkTags(bookmarkTags);

        List<BookmarkTag> savedBookmarkTags = mapToBookmarkTags(bookmark, requestDto.getTags());
        bookmarkTagRepository.saveAll(savedBookmarkTags);
    }

    public List<BookmarkTagResponse.Info> getAllBookmarkTags(String username) {
        List<BookmarkTagProjection.BookmarkAndTag> bookmarkTagsByUserId = bookmarkTagQueryRepository.findAllBookmarkTagsByUserId(username);
        return mapToBookmarkTagResponse(bookmarkTagsByUserId);
    }

    public List<BookmarkResponse.Info> getBookmarkTagsByTagList(String username, List<Long> tagIds) {
        List<BookmarkTagProjection.BookmarkAndTag> bookmarkTagsByUserIdAndTagList = bookmarkTagQueryRepository.findBookmarkTagsByUserIdAndTagList(username, tagIds);
        Map<Tag, List<Bookmark>> bookmarkTagToHashMap = getBookmarkTagToHashMap(bookmarkTagsByUserIdAndTagList);

        return removeBookmarkDuplication(bookmarkTagToHashMap);
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
        Map<Tag, List<Bookmark>> bookmarkTagMap = getBookmarkTagToHashMap(bookmarkByUserIdAndTags);

        List<BookmarkTagResponse.Info> bookmarkTagResponses = new ArrayList<>();
        for (Tag key : bookmarkTagMap.keySet()) {
            bookmarkTagResponses.add(new BookmarkTagResponse.Info(key, bookmarkTagMap.get(key)));
        }
        return bookmarkTagResponses;
    }

    private static Map<Tag, List<Bookmark>> getBookmarkTagToHashMap(List<BookmarkTagProjection.BookmarkAndTag> bookmarkByUserIdAndTags) {
        Map<Tag, List<Bookmark>> bookmarkTagMap = new HashMap<>();
        for (BookmarkTagProjection.BookmarkAndTag bookmarkByUserIdAndTag : bookmarkByUserIdAndTags) {
            bookmarkTagMap.put(bookmarkByUserIdAndTag.getTag(), bookmarkTagMap.getOrDefault(bookmarkByUserIdAndTag.getTag(), new ArrayList<>()));
            bookmarkTagMap.get(bookmarkByUserIdAndTag.getTag()).add(bookmarkByUserIdAndTag.getBookmark());
        }

        return bookmarkTagMap;
    }

    private List<BookmarkResponse.Info> removeBookmarkDuplication(Map<Tag, List<Bookmark>> bookmarkTagMap) {
        Set<Bookmark> set = new HashSet<>();
        for (Tag key : bookmarkTagMap.keySet()) {
            List<Bookmark> bookmarks = bookmarkTagMap.get(key);
            set.addAll(bookmarks);
        }

        return set.stream()
                .map(bookmark -> BookmarkResponse.Info.builder()
                        .id(bookmark.getId())
                        .url(bookmark.getUrl())
                        .title(bookmark.getTitle())
                        .description(bookmark.getDescription())
                        .isPublic(bookmark.isPublic())
                        .isStarred(bookmark.isStarred())
                        .image(bookmark.getMetaImage())
                        .createdAt(bookmark.getCreatedAt().toLocalDate())
                        .modifiedAt(bookmark.getModifiedAt().toLocalDate())
                        .build())
                .collect(Collectors.toList());
    }
}
