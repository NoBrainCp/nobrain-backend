package com.nobrain.bookmarking.domain.category.controller;

import com.nobrain.bookmarking.domain.category.dto.request.AddCategoryRequest;
import com.nobrain.bookmarking.domain.category.service.CategoryService;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.service.ResponseService;
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
    public CommonResult addCategory(@Valid @RequestBody AddCategoryRequest dto) {
        categoryService.addCategory(dto);
        SingleResult<String> result = new SingleResult<>();
        result.setData(dto.getName());
        responseService.setSuccessResult(result);
        return result;
    }
}
