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
@RequestMapping("${app.domain}")
public class TagController {

    private final TagService tagService;
    private final ResponseService responseService;

    @GetMapping("/user/{userId}/tags")
    public ListResult<TagResponse.Info> getAllTagsOfUser(@PathVariable Long userId) {
        return responseService.getListResult(tagService.getAllTagsOfUser(userId));
    }
}
