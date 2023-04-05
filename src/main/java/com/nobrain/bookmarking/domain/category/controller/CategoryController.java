package com.nobrain.bookmarking.domain.category.controller;

import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.category.dto.CategoryRequest;
import com.nobrain.bookmarking.domain.category.dto.CategoryResponse;
import com.nobrain.bookmarking.domain.category.service.CategoryService;
import com.nobrain.bookmarking.domain.user.annotation.VerifiedUser;
import com.nobrain.bookmarking.global.response.model.CommonResult;
import com.nobrain.bookmarking.global.response.model.ListResult;
import com.nobrain.bookmarking.global.response.model.SingleResult;
import com.nobrain.bookmarking.global.response.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("${app.domain}/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ResponseService responseService;

    @GetMapping("/{categoryName}/users/{username}")
    public SingleResult<CategoryResponse.Header> getCategoryHeader(@PathVariable String username,
                                                                   @PathVariable String categoryName) {
        return responseService.getSingleResult(categoryService.getCategoryHeader(username, categoryName));
    }

    @GetMapping("/users/{username}")
    public ListResult<CategoryResponse.Info> getCategories(@PathVariable String username) {
        return responseService.getListResult(categoryService.getCategories(username));
    }

    @GetMapping("/bookmarks/{bookmarkId}")
    public SingleResult<CategoryResponse.Info> getCategoryByBookmarkId(@PathVariable Long bookmarkId) {
        return responseService.getSingleResult(categoryService.getCategoryByBookmarkId(bookmarkId));
    }

    @GetMapping("/{categoryName}/public/users/{userId}")
    public SingleResult<Boolean> getCategoryIsPublic(@PathVariable Long userId,
                                                     @PathVariable String categoryName) {
        return responseService.getSingleResult(categoryService.getCategoryIdPublic(userId, categoryName));
    }

    @PostMapping("")
    public SingleResult<String> addCategory(
            @VerifiedUser UserPayload payload,
            @Valid @RequestBody CategoryRequest.Info requestDto) {
        return responseService.getSingleResult(categoryService.create(payload, requestDto));
    }

    @PutMapping("/{categoryName}")
    public CommonResult updateCategory(@VerifiedUser UserPayload payload,
                                       @PathVariable String categoryName,
                                       @RequestBody CategoryRequest.Info requestDto) {
        categoryService.updateCategory(payload, categoryName, requestDto);
        return responseService.getSuccessResult();
    }

    @DeleteMapping("/{categoryName}")
    public CommonResult deleteCategory(@VerifiedUser UserPayload payload,
                                       @PathVariable String categoryName) {
        categoryService.deleteCategory(payload, categoryName);
        return responseService.getSuccessResult();
    }
}
