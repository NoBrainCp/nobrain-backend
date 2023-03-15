package com.nobrain.bookmarking.domain.bookmark_tag.controller;

import com.nobrain.bookmarking.domain.bookmark_tag.dto.BookmarkTagResponse;
import com.nobrain.bookmarking.domain.bookmark_tag.service.BookmarkTagService;
import com.nobrain.bookmarking.global.response.model.ListResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}")
public class BookmarkTagController {

    private final BookmarkTagService bookmarkTagService;
    private final ResponseService responseService;

    @GetMapping("/user/{username}/bookmark-tags")
    public ListResult<BookmarkTagResponse.Info> getAllBookmarkTags(@PathVariable String username) {
        return responseService.getListResult(bookmarkTagService.getAllBookmarkTags(username));
    }

    @GetMapping("/user/{userId}/bookmark-tags/tags")
    public ListResult<BookmarkTagResponse.Info> getBookmarkTagsByTagList(@PathVariable Long userId, @RequestParam List<Long> tagIds) {
        return responseService.getListResult(bookmarkTagService.getBookmarkTagsByTagList(userId, tagIds));
    }
}
