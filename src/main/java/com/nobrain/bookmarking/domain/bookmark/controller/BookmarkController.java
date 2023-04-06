package com.nobrain.bookmarking.domain.bookmark.controller;

import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkResponse;
import com.nobrain.bookmarking.domain.bookmark.service.BookmarkService;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.model.ListResult;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}/bookmarks")
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final ResponseService responseService;

    @GetMapping("/users/{username}")
    public ListResult<BookmarkResponse.Info> getAllBookmarksByUsername(@PathVariable String username) {
        return responseService.getListResult(bookmarkService.getAllBookmarksByUsername(username));
    }

    @GetMapping("/users/{username}/category/{categoryName}")
    public ListResult<BookmarkResponse.Info> getBookmarksByCategory(
            @PathVariable String username,
            @PathVariable String categoryName) {
        return responseService.getListResult(bookmarkService.getBookmarksByCategory(username, categoryName));
    }

    @GetMapping("/starred/users/{username}")
    public ListResult<BookmarkResponse.Info> getStarredBookmarks(@PathVariable String username) {
        return responseService.getListResult(bookmarkService.getStarredBookmarks(username));
    }

    @GetMapping("/starred/count/users/{username}")
    public SingleResult<Long> getStarredBookmarksCount(@PathVariable String username) {
        return responseService.getSingleResult(bookmarkService.getStarredBookmarksCount(username));
    }

    @GetMapping("/private/user/{username}")
    public ListResult<BookmarkResponse.Info> getPrivateBookmarks(@PathVariable String username) {
        return responseService.getListResult(bookmarkService.getPrivateBookmarks(username));
    }

    @GetMapping("/private/count/user/{username}")
    public SingleResult<Long> getPrivateBookmarksCount(@PathVariable String username) {
        return responseService.getSingleResult(bookmarkService.getPrivateBookmarksCount(username));
    }

    @GetMapping("/search")
    public ListResult<BookmarkResponse.Info> searchBookmarks(@RequestParam String keyword,
                                                             @RequestParam String condition) {
        return responseService.getListResult(bookmarkService.searchBookmarks(keyword, condition.toLowerCase()));
    }

    @PostMapping("/users/{username}")
    public CommonResult addBookmark(@PathVariable String username, @RequestBody @Valid BookmarkRequest.Info requestDto) {
        bookmarkService.createBookmark(username, requestDto);
        return responseService.getSuccessResult();
    }

    @PutMapping("/{bookmarkId}")
    public CommonResult updateBookmark(@PathVariable Long bookmarkId, @RequestBody @Valid BookmarkRequest.Info requestDto) {
        bookmarkService.updateBookmark(bookmarkId, requestDto);
        return responseService.getSuccessResult();
    }

    @PutMapping("/{bookmarkId}/starred")
    public CommonResult updateStarred(@PathVariable Long bookmarkId, @RequestParam Boolean isStarred) {
        bookmarkService.updateStarred(bookmarkId, isStarred);
        return responseService.getSuccessResult();
    }

    @PutMapping("/{bookmarkId}/public")
    public CommonResult updatePublic(@PathVariable Long bookmarkId, @RequestParam Boolean isPublic) {
        bookmarkService.updatePublic(bookmarkId, isPublic);
        return responseService.getSuccessResult();
    }

    @PutMapping("/private/user/{userId}/category/{categoryName}")
    public CommonResult updateAllBookmarksToPrivate(@PathVariable Long userId, @PathVariable String categoryName) {
        bookmarkService.updateAllBookmarksToPrivate(userId, categoryName);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/{bookmarkId}")
    public CommonResult deleteBookmark(@PathVariable Long bookmarkId) {
        bookmarkService.deleteBookmark(bookmarkId);
        return responseService.getSuccessResult();
    }
}
