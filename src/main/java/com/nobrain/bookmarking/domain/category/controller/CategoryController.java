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
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;
    private final ResponseService responseService;

    @GetMapping("/{username}")
    public ListResult<CategoryResponse.Info> getCategories(@PathVariable String username) {
        return responseService.getListResult(categoryService.getCategories(username));
    }

    @PostMapping("/add-category")
    public SingleResult<String> addCategory(@Valid @RequestBody CategoryRequest.Create dto) {
        return responseService.getSingleResult(categoryService.create(dto));
    }
}
