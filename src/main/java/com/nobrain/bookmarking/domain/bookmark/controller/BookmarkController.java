package com.nobrain.bookmarking.domain.bookmark.controller;

import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkResponse;
import com.nobrain.bookmarking.domain.bookmark.service.BookmarkService;
import com.nobrain.bookmarking.domain.tag.service.TagService;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.model.ListResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final TagService tagService;
    private final ResponseService responseService;

//    @GetMapping("/{username}/bookmarks")
//    public ListResult<BookmarkResponse.Info> getBookmarks(@PathVariable String username) {
//
//    }

    @GetMapping("/{username}/{category}/bookmarks")
    public ListResult<BookmarkResponse.Info> getBookmarksInCategory(
            @PathVariable String username,
            @PathVariable String category) {
        return responseService.getListResult(bookmarkService.getBookmarksByCategory(username, category));
    }

    @PostMapping("/bookmark")
    public CommonResult addBookmark(@RequestBody BookmarkRequest.Create dto) {
        tagService.createTags(bookmarkService.createBookmark(dto));
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/bookmark/{bookmarkId}")
    public CommonResult deleteBookmark(@PathVariable int bookmarkId) {
        return responseService.getSuccessResult();
    }
}
