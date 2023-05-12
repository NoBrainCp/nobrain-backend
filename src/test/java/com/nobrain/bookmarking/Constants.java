package com.nobrain.bookmarking;

public class Constants {

    private Constants() {}

    public static final String BASE_URL = "/api/v1";

    // user
    public static final Long USER_ID = 1L;
    public static final String USERNAME = "test";
    public static final String NEW_USERNAME = "newName";
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "testPassword123!";
    public static final String PASSWORD_CHECK = "testPassword123!";
    public static final String PASSWORD_NOT_SAME = "notSamePassword";
    public static final String PASSWORD_CHECK_NOT_SAME = "notSameTestPassword";
    public static final String CHANGE_PASSWORD = "changePassword";
    public static final String CHANGE_PASSWORD_NOT_SAME = "notSameChangePassword";
    public static final String PROFILE_IMG = "profileImage";
    public static final String CHANGE_PROFILE_IMG_NAME = "changeProfileImage";
    public static final String CHANGE_PROFILE_IMG_PATH = "changeProfileImage.png";
    public static final String PROFILE_IMG_CONTENT_TYPE_PNG = "image/png";

    // category
    public static final Long CATEGORY_ID = 1L;
    public static final Long CATEGORY_BOOKMARK_COUNT = 0L;
    public static final String CATEGORY_NAME = "categoryTest";
    public static final String NEW_CATEGORY_NAME = "newCategoryTest";
    public static final String CATEGORY_DESCRIPTION = "test";
    public static final boolean CATEGORY_PUBLIC = true;

    // bookmark
    public static final Long BOOKMARK_ID = 1L;
}
