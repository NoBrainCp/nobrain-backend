package com.nobrain.bookmarking.domain.bookmark.controller;

import com.nobrain.bookmarking.domain.bookmark.dto.BookmarkRequest;
import com.nobrain.bookmarking.domain.bookmark.service.BookmarkService;
import com.nobrain.bookmarking.domain.tag.service.TagService;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private final TagService tagService;
    private final ResponseService responseService;

    @PostMapping({"/{username}/{categoryName}/add-bookmark", "/{username}/add-bookmark"})
    public CommonResult addBookmark(@PathVariable String username,
                                    @RequestBody BookmarkRequest.AddBookmarkRequest dto) {

        tagService.createTags(bookmarkService.createBookmark(username, dto));
        return responseService.getSuccessResult();
    }
}
