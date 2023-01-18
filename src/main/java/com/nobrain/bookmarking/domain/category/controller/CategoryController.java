package com.nobrain.bookmarking.domain.category.controller;

import com.nobrain.bookmarking.domain.category.dto.request.CategoryRequest;
import com.nobrain.bookmarking.domain.category.service.CategoryService;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class CategoryController {

    private final CategoryService categoryService;
    private final ResponseService responseService;

    @PostMapping("/add-category")
    public SingleResult<String> addCategory(@Valid @RequestBody CategoryRequest.Create dto) {
        return responseService.getSingleResult(categoryService.create(dto));
    }
}
