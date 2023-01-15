package com.nobrain.bookmarking.domain.bookmark.controller;

import com.nobrain.bookmarking.domain.bookmark.dto.request.AddBookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.service.BookmarkService;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final ResponseService responseService;

    @PostMapping("/{username}/{categoryName}/add-bookmark")
    public CommonResult addBookmark(@PathVariable String username,
                                    @PathVariable String categoryName,
                                    @RequestBody AddBookmarkRequest dto) {
        bookmarkService.createBookmark(username, categoryName, dto);
        return responseService.getSuccessResult();
    }
}
