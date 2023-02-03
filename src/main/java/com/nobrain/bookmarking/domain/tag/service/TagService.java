package com.nobrain.bookmarking.domain.tag.service;

import com.nobrain.bookmarking.domain.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

//    @Transactional
//    public void createTags(Bookmark bookmark) {
//        List<Tag> tags = new CopyOnWriteArrayList<>(bookmark.getTags());
//        for (Tag tag : tags) {
//            tag.addBookmark(bookmark);
//        }
//
//        tagRepository.saveAll(bookmark.getTags());
//    }
}
