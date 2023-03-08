package com.nobrain.bookmarking.domain.bookmark.search;

public enum SearchCondition {

    MY("my"),
    FOLLOW("follow"),
    ALL("all");

    private final String condition;

    SearchCondition(String condition) {
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
