package com.nobrain.bookmarking.domain.bookmark.service;

import com.nobrain.bookmarking.domain.bookmark.dto.request.AddBookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.entity.Bookmark;
import com.nobrain.bookmarking.domain.bookmark.repository.BookmarkRepository;
import com.nobrain.bookmarking.domain.category.repository.CategoryRepository;
import com.nobrain.bookmarking.domain.tag.entity.Tag;
import com.nobrain.bookmarking.domain.tag.repository.TagRepository;
import com.nobrain.bookmarking.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Transactional
    public void createBookmark(String userName, String categoryName, AddBookmarkRequest dto) {
        Set<Tag> tags = new HashSet<>();
        for (String tagName : dto.getTags().split(" ")) {
            Tag tag = Tag.builder().name(tagName).build();
            tags.add(tag);
        }

        Bookmark bookmark = Bookmark.builder()
                .url(dto.getUrl())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .isPublic(dto.isPublic())
                .isStar(dto.isStar())
                .tags(tags)
                .build();

        bookmark.addUser(userRepository.findByName(userName).get());
        bookmark.addCategory(categoryRepository.findByName(categoryName).get());
        bookmarkRepository.save(bookmark);

        for (Tag tag : tags) {
            tag.addBookmark(bookmark);
            tagRepository.save(tag);
        }
    }
}
