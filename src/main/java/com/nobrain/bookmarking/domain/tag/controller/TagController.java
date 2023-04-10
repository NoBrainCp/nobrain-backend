package com.nobrain.bookmarking.domain.tag.controller;

import com.nobrain.bookmarking.domain.tag.dto.TagResponse;
import com.nobrain.bookmarking.domain.tag.service.TagService;
import com.nobrain.bookmarking.global.response.model.ListResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}/tags")
public class TagController {

    private final TagService tagService;
    private final ResponseService responseService;

    @GetMapping("/users/{username}")
    public ListResult<TagResponse.Info> getAllTagsOfUser(@PathVariable String username) {
        return responseService.getListResult(tagService.getAllTagsOfUser(username));
    }

    @GetMapping("/users/{username}/bookmark/{bookmarkId}")
    public ListResult<TagResponse.Info> getTagsOfUserByBookmarkId(@PathVariable String username, @PathVariable Long bookmarkId) {
        return responseService.getListResult(tagService.getTagsOfUserByBookmarkId(username, bookmarkId));
    }
}
