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
    public static final String URL = "www.naver.com";
    public static final String CHANGE_URL = "www.google.com";
    public static final String HTTPS = "https://";
    public static final String TITLE = "네이버";
    public static final String CHANGE_TITLE = "구글";
    public static final String DESCRIPTION = "네이버 웹 사이트";
    public static final String CHANGE_DESCRIPTION = "구글 웹 사이트";
    public static final boolean IS_PUBLIC = true;
    public static final boolean IS_PRIVATE = false;
    public static final boolean IS_STARRED = true;
    public static final boolean IS_NOT_STARRED = false;
    public static final String META_IMG = "";
    public static final String BOOKMARK_SEARCH_CONDITION = "all";
    public static final String BOOKMARK_SEARCH_KEYWORD = "네";


    // tag
    public static final Long TAG_ID = 1L;
    public static final String TAG_NAME = "tagTest";

    // follow
    public static final Long FOLLOW_ID = 1L;
    public static final int FOLLOWER_COUNT = 1;
    public static final int FOLLOWING_COUNT = 1;
    public static final Long TO_USER_ID = 2L;
    public static final String TO_USERNAME = "toUserName";
    public static final String TO_USER_EMAIL = "toUser@email.com";
    public static final String TO_USER_PASSWORD = "toUserPassword12!";
    public static final String TO_USER_PROFILE_IMG = "";
    public static final Long FROM_USER_BOOKMARK_COUNT = 1L;
    public static final boolean IS_FOLLOW = true;
}
