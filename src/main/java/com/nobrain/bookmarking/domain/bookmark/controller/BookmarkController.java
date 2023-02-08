package com.nobrain.bookmarking.domain.bookmark.controller;

import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkResponse;
import com.nobrain.bookmarking.domain.bookmark.service.BookmarkService;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
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
    private final ResponseService responseService;

    @GetMapping("/{username}/bookmarks")
    public ListResult<BookmarkResponse.Info> getAllBookmarksByUsername(@PathVariable String username) {
        return responseService.getListResult(bookmarkService.getAllBookmarksByUsername(username));
    }

    @GetMapping("/{username}/{category}/bookmarks")
    public ListResult<BookmarkResponse.Info> getBookmarksInCategory(
            @PathVariable String username,
            @PathVariable String category) {
        return responseService.getListResult(bookmarkService.getBookmarksByCategory(username, category));
    }

    @PostMapping("/{username}/bookmark")
    public CommonResult addBookmark(@PathVariable String username, @RequestBody BookmarkRequest.Info requestDto) {
        bookmarkService.createBookmark(username, requestDto);
        return responseService.getSuccessResult();
    }

    @PutMapping("/bookmark/{bookmarkId}")
    public CommonResult updateBookmark(@PathVariable long bookmarkId, @RequestBody BookmarkRequest.Info requestDto) {
        bookmarkService.updateBookmark(bookmarkId, requestDto);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/bookmark/{bookmarkId}")
    public CommonResult deleteBookmark(@PathVariable long bookmarkId) {
        bookmarkService.deleteBookmark(bookmarkId);
        return responseService.getSuccessResult();
    }
}
