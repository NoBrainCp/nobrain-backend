package com.nobrain.bookmarking.domain.bookmark.controller;

import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkResponse;
import com.nobrain.bookmarking.domain.bookmark.service.BookmarkService;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.model.ListResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @GetMapping("/bookmark/search")
    public ListResult<BookmarkResponse.Info> searchBookmark(@RequestParam String keyword, @RequestParam String condition) {
        return responseService.getListResult(bookmarkService.searchBookmark(keyword, condition.toLowerCase()));
    }

    @PostMapping("/{username}/bookmark")
    public CommonResult addBookmark(@PathVariable String username, @RequestBody @Valid BookmarkRequest.Info requestDto) {
        bookmarkService.createBookmark(username, requestDto);
        return responseService.getSuccessResult();
    }

    @PutMapping("/bookmark/{bookmarkId}")
    public CommonResult updateBookmark(@PathVariable Long bookmarkId, @RequestBody @Valid BookmarkRequest.Info requestDto) {
        bookmarkService.updateBookmark(bookmarkId, requestDto);
        return responseService.getSuccessResult();
    }

    @PutMapping("/bookmark/{bookmarkId}/starred")
    public CommonResult updateStarred(@PathVariable Long bookmarkId, @RequestParam Boolean isStarred) {
        bookmarkService.updateStarred(bookmarkId, isStarred);
        return responseService.getSuccessResult();
    }

    @PutMapping("/bookmark/{bookmarkId}/public")
    public CommonResult updatePublic(@PathVariable Long bookmarkId, @RequestParam Boolean isPublic) {
        bookmarkService.updatePublic(bookmarkId, isPublic);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/bookmark/{bookmarkId}")
    public CommonResult deleteBookmark(@PathVariable Long bookmarkId) {
        bookmarkService.deleteBookmark(bookmarkId);
        return responseService.getSuccessResult();
    }
}
