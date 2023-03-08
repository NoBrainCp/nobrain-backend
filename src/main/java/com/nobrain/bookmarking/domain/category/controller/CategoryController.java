package com.nobrain.bookmarking.domain.category.controller;

import com.nobrain.bookmarking.domain.category.dto.CategoryRequest;
import com.nobrain.bookmarking.domain.category.dto.CategoryResponse;
import com.nobrain.bookmarking.domain.category.service.CategoryService;
import com.nobrain.bookmarking.global.response.model.CommonResult;
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

    @GetMapping("user/{userId}/categories")
    public ListResult<CategoryResponse.Info> getCategories(@PathVariable Long userId) {
        return responseService.getListResult(categoryService.getCategories(userId));
    }

    @PostMapping("/{username}/category")
    public SingleResult<String> addCategory(
            @PathVariable String username,
            @Valid @RequestBody CategoryRequest.Info requestDto) {
        return responseService.getSingleResult(categoryService.create(username, requestDto));
    }

    @PutMapping("{username}/category/{categoryName}")
    public CommonResult updateCategory(@PathVariable String username,
                                       @PathVariable String categoryName,
                                       @RequestBody CategoryRequest.Info requestDto) {
        categoryService.updateCategory(username, categoryName, requestDto);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("{username}/category/{categoryName}")
    public CommonResult deleteCategory(@PathVariable String username, @PathVariable String categoryName) {
        categoryService.deleteCategory(username, categoryName);
        return responseService.getSuccessResult();
    }
}
