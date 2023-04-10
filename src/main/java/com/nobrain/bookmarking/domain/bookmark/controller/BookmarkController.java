package com.nobrain.bookmarking.domain.bookmark.controller;

import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkResponse;
import com.nobrain.bookmarking.domain.bookmark.service.BookmarkService;
import com.nobrain.bookmarking.domain.user.annotation.VerifiedUser;
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
    public ListResult<BookmarkResponse.Info> getBookmarksByUsername(@VerifiedUser UserPayload myPayload, @PathVariable String username) {
        return responseService.getListResult(bookmarkService.getBookmarksByUsername(myPayload, username));
    }

    @GetMapping("/users/{username}/categories/{categoryName}")
    public ListResult<BookmarkResponse.Info> getBookmarksByCategory(@VerifiedUser UserPayload myPayload,
                                                                    @PathVariable String username,
                                                                    @PathVariable String categoryName) {
        return responseService.getListResult(bookmarkService.getBookmarksByCategory(myPayload, username, categoryName));
    }

    @GetMapping("/starred/users/{username}")
    public ListResult<BookmarkResponse.Info> getStarredBookmarks(@VerifiedUser UserPayload myPayload, @PathVariable String username) {
        return responseService.getListResult(bookmarkService.getStarredBookmarks(myPayload, username));
    }

    @GetMapping("/starred/count/users/{username}")
    public SingleResult<Long> getStarredBookmarksCount(@VerifiedUser UserPayload myPayload, @PathVariable String username) {
        return responseService.getSingleResult(bookmarkService.getStarredBookmarksCount(myPayload, username));
    }

    @GetMapping("/private")
    public ListResult<BookmarkResponse.Info> getPrivateBookmarks(@VerifiedUser UserPayload myPayload) {
        return responseService.getListResult(bookmarkService.getPrivateBookmarks(myPayload));
    }

    @GetMapping("/private/count")
    public SingleResult<Long> getPrivateBookmarksCount(@VerifiedUser UserPayload myPayload) {
        return responseService.getSingleResult(bookmarkService.getPrivateBookmarksCount(myPayload));
    }

    @GetMapping("/search")
    public ListResult<BookmarkResponse.Info> searchBookmarks(@VerifiedUser UserPayload myPayload,
                                                             @RequestParam String keyword,
                                                             @RequestParam String condition) {
        return responseService.getListResult(bookmarkService.searchBookmarks(myPayload, keyword, condition.toLowerCase()));
    }

    @PostMapping("")
    public CommonResult addBookmark(@VerifiedUser UserPayload myPayload,
                                    @RequestBody @Valid BookmarkRequest.Info requestDto) {
        bookmarkService.createBookmark(myPayload, requestDto);
        return responseService.getSuccessResult();
    }

    @PutMapping("/{bookmarkId}")
    public CommonResult updateBookmark(@PathVariable Long bookmarkId,
                                       @RequestBody @Valid BookmarkRequest.Info requestDto) {
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

    @PutMapping("/private/categories/{categoryName}")
    public CommonResult updateBookmarksToPrivate(@VerifiedUser UserPayload myPayload, @PathVariable String categoryName) {
        bookmarkService.updateBookmarksToPrivate(myPayload, categoryName);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/{bookmarkId}")
    public CommonResult deleteBookmark(@PathVariable Long bookmarkId) {
        bookmarkService.deleteBookmark(bookmarkId);
        return responseService.getSuccessResult();
    }
}
