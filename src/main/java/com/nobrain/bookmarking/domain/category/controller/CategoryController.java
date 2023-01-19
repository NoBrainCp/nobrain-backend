package com.nobrain.bookmarking.domain.category.controller;

import com.nobrain.bookmarking.domain.category.dto.CategoryRequest;
import com.nobrain.bookmarking.domain.category.dto.CategoryResponse;
import com.nobrain.bookmarking.domain.category.service.CategoryService;
import com.nobrain.bookmarking.global.response.model.ListResult;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}")
public class CategoryController {

    private final CategoryService categoryService;
    private final ResponseService responseService;

    @GetMapping("/{username}/get-categories")
    public ListResult<CategoryResponse.Info> getCategories(@PathVariable String username) {
        return responseService.getListResult(categoryService.getCategories(username));
    }

    @PostMapping("/{username}/add-category")
    public SingleResult<String> addCategory(
            @PathVariable String username,
            @Valid @RequestBody CategoryRequest.Create dto) {
        return responseService.getSingleResult(categoryService.create(username, dto));
    }
}
