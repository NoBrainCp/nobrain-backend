package com.nobrain.bookmarking.domain.category.service;

import com.nobrain.bookmarking.ServiceTest;
import com.nobrain.bookmarking.domain.auth.dto.UserPayload;
import com.nobrain.bookmarking.domain.category.dto.CategoryRequest;
import com.nobrain.bookmarking.domain.category.dto.CategoryResponse;
import com.nobrain.bookmarking.domain.category.entity.Category;
import com.nobrain.bookmarking.domain.category.exception.CategoryNameDuplicationException;
import com.nobrain.bookmarking.domain.category.exception.CategoryNotFoundException;
import com.nobrain.bookmarking.domain.user.entity.User;
import com.nobrain.bookmarking.domain.user.exception.UserNotFoundException;
import com.nobrain.bookmarking.global.security.PasswordEncryptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.nobrain.bookmarking.Constants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class CategoryServiceTest extends ServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PasswordEncryptor encryptor;

    private User user;
    private UserPayload payload;
    private Category category;

    private final CategoryRequest.Info categoryInfoRequest = new CategoryRequest.Info(
            NEW_CATEGORY_NAME,
            CATEGORY_DESCRIPTION,
            CATEGORY_PUBLIC
    );

    private final CategoryResponse.Info categoryInfoResponse = new CategoryResponse.Info(
            CATEGORY_ID,
            CATEGORY_NAME,
            CATEGORY_DESCRIPTION,
            CATEGORY_PUBLIC,
            CATEGORY_BOOKMARK_COUNT
    );

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(USER_ID)
                .name(USERNAME)
                .email(EMAIL)
                .password(encryptor.encrypt(PASSWORD))
                .profileImage(PROFILE_IMG)
                .categories(new ArrayList<>())
                .build();

        payload = UserPayload.builder()
                .userId(USER_ID)
                .username(USERNAME)
                .build();

        category = Category.builder()
                .name(CATEGORY_NAME)
                .description(CATEGORY_DESCRIPTION)
                .isPublic(CATEGORY_PUBLIC)
                .user(user)
                .bookmarks(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("단일 카테고리 조회 - 성공")
    void getCategory() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));

        given(categories.findByUserAndName(any(User.class), anyString()))
                .willReturn(Optional.of(category));
        // when
        CategoryResponse.Header actual = categoryService.getCategory(user.getName(), category.getName());

        // then
        CategoryResponse.Header expected = CategoryResponse.Header.toDto(category);
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(expected);

        verify(users).findByName(user.getName());
        verify(categories).findByUserAndName(user, category.getName());
    }

    @Test
    @DisplayName("단일 카테고리 조회 - 존재하지 않는 유저 실패")
    void getCategory_user_not_found() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> categoryService.getCategory(user.getName(), category.getName()))
                .isInstanceOf(UserNotFoundException.class);

        verify(users).findByName(user.getName());
    }

    @Test
    @DisplayName("단일 카테고리 조회 - 존재하지 않는 카테고리 실패")
    void getCategory_category_not_found() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));

        // when, then
       assertThatThrownBy(() -> categoryService.getCategory(user.getName(), category.getName()))
               .isInstanceOf(CategoryNotFoundException.class);

        verify(users).findByName(user.getName());
    }

    @Test
    @DisplayName("카테고리 목록 조회 - 성공")
    void getCategories() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));
        given(categoryQueryRepository.findAllCategoryInfoWithCount(anyString(), anyBoolean()))
                .willReturn(List.of(categoryInfoResponse));
        // when
        List<CategoryResponse.Info> actual = categoryService.getCategories(payload, user.getName());

        //then
        assertThat(actual.get(0)).usingRecursiveComparison()
                        .isEqualTo(categoryInfoResponse);

        verify(users).findByName(user.getName());
        verify(categoryQueryRepository).findAllCategoryInfoWithCount(user.getName(), true);
    }

    @Test
    @DisplayName("카테고리 목록 조회 - 존재 하지 않는 유저 실패")
    void getCategories_user_not_found() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> categoryService.getCategories(payload, user.getName()))
                .isInstanceOf(UserNotFoundException.class);

        verify(users).findByName(user.getName());
    }

    @Test
    @DisplayName("카테고리 조회 (북마크 아이디) - 성공")
    void getCategoryByBookmarkId() {
        // given
        given(categoryQueryRepository.findCategoryByBookmarkId(anyLong()))
                .willReturn(categoryInfoResponse);

        // when
        CategoryResponse.Info actual = categoryService.getCategoryByBookmarkId(BOOKMARK_ID);

        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(categoryInfoResponse);
        verify(categoryQueryRepository).findCategoryByBookmarkId(BOOKMARK_ID);
    }

    @Test
    @DisplayName("카테고리 공개 여부 조회 - 성공")
    void getCategoryIdPublic() {
        // given
        given(users.findByName(anyString()))
               .willReturn(Optional.of(user));
        given(categories.findByUserAndName(any(User.class), anyString()))
                .willReturn(Optional.of(category));
        // when
        Boolean actual = categoryService.getCategoryIsPublic(payload, category.getName());

        // then
        assertThat(actual).isEqualTo(category.isPublic());

        verify(users).findByName(user.getName());
        verify(categories).findByUserAndName(user, category.getName());
    }

    @Test
    @DisplayName("카테고리 공개 여부 조회 - 존재하지 않는 유저 실패")
    void getCategoryIdPublic_user_not_found() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> categoryService.getCategoryIsPublic(payload, category.getName()))
                .isInstanceOf(UserNotFoundException.class);

        verify(users).findByName(user.getName());
    }

    @Test
    @DisplayName("카테고리 공개 여부 조회 - 존재하지 않는 카테고리 실패")
    void getCategoryIdPublic_category_not_found() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> categoryService.getCategoryIsPublic(payload, category.getName()))
                .isInstanceOf(CategoryNotFoundException.class);

        verify(users).findByName(user.getName());
    }

    @Test
    @DisplayName("카테고리 생성 - 성공")
    void createCategory() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));
        given(categories.save(any(Category.class)))
                .willReturn(categoryInfoRequest.toEntity(user));

        // when
        String actual = categoryService.create(payload, categoryInfoRequest);

        // then
        assertThat(actual).isEqualTo(NEW_CATEGORY_NAME);

        verify(users).findByName(user.getName());
        verify(categories).save(categoryInfoRequest.toEntity(user));
    }

    @Test
    @DisplayName("카테고리 생성 - 존재하지 않는 유저 실패")
    void createCategory_user_not_found() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> categoryService.create(payload, categoryInfoRequest))
                .isInstanceOf(UserNotFoundException.class);

        verify(users).findByName(user.getName());
    }

    @Test
    @DisplayName("카테고리 생성 - 동일한 카테고리 이름 실패")
    void createCategory_duplication_category_name() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));
        given(categories.existsByUserAndName(any(User.class), anyString()))
                .willReturn(true);

        CategoryRequest.Info categoryInfoRequest = new CategoryRequest.Info(
                CATEGORY_NAME,
                CATEGORY_DESCRIPTION,
                CATEGORY_PUBLIC
        );
        // when, then
        assertThatThrownBy(() -> categoryService.create(payload, categoryInfoRequest))
                .isInstanceOf(CategoryNameDuplicationException.class);

        verify(users).findByName(user.getName());
        verify(categories).existsByUserAndName(user, categoryInfoRequest.getName());
    }


    @Test
    @DisplayName("카테고리 갱신 - 성공")
    void updateCategory() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));
        given(categories.findByUserAndName(any(User.class), anyString()))
                .willReturn(Optional.of(category));
        // when
        categoryService.updateCategory(payload, category.getName(), categoryInfoRequest);

        // then
        assertThat(category.getName()).isEqualTo(NEW_CATEGORY_NAME);

        verify(users).findByName(user.getName());
        verify(categories).findByUserAndName(user, CATEGORY_NAME);
    }

    @Test
    @DisplayName("카테고리 갱신 - 동일한 카테고리 이름 실패")
    void updateCategory_duplication_category_name() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatThrownBy(() -> categoryService.updateCategory(payload, category.getName(), categoryInfoRequest))
                .isInstanceOf(CategoryNotFoundException.class);

        verify(users).findByName(user.getName());
    }

    @Test
    @DisplayName("카테고리 갱신 - 이미 존재하는 카테고리 이름 실패")
    void updateCategory_exists_category_name() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));
        given(categories.findByUserAndName(any(User.class), anyString()))
                .willReturn(Optional.of(category));
        given(categories.existsByUserAndName(any(User.class), anyString()))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> categoryService.updateCategory(payload, category.getName(), categoryInfoRequest))
                .isInstanceOf(CategoryNameDuplicationException.class);

        verify(users).findByName(user.getName());
        verify(categories).findByUserAndName(user, category.getName());
        verify(categories).existsByUserAndName(user, categoryInfoRequest.getName());
    }

    @Test
    @DisplayName("카테고리 삭제 - 성공")
    void deleteCategory() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.of(user));

        // when, then
        assertThatCode(() -> categoryService.deleteCategory(payload, category.getName()))
                .doesNotThrowAnyException();

        verify(users).findByName(user.getName());
    }

    @Test
    @DisplayName("카테고리 삭제 - 존재하지 않는 유저 실패")
    void deleteCategory_exists_user() {
        // given
        given(users.findByName(anyString()))
                .willReturn(Optional.empty());

        // when, then
        assertThatCode(() -> categoryService.deleteCategory(payload, category.getName()))
                .isInstanceOf(UserNotFoundException.class);

        verify(users).findByName(user.getName());
    }
}


