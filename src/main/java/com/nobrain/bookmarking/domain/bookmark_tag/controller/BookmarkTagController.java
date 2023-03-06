package com.nobrain.bookmarking.domain.bookmark_tag.controller;

import com.nobrain.bookmarking.domain.bookmark_tag.dto.BookmarkTagResponse;
import com.nobrain.bookmarking.domain.bookmark_tag.service.BookmarkTagService;
import com.nobrain.bookmarking.global.response.model.ListResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}")
public class BookmarkTagController {

    private final BookmarkTagService bookmarkTagService;
    private final ResponseService responseService;

    @GetMapping("/user/{userId}/bookmark-tags")
    private ListResult<BookmarkTagResponse.Info> getAllBookmarkTags(@PathVariable Long userId) {
        return responseService.getListResult(bookmarkTagService.getAllBookmarkTags(userId));
    }
}
