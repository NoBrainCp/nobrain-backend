package com.nobrain.bookmarking.domain.bookmark.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBookmarkRequest {
    private String url;
    private String title;
    private String description;

    @JsonProperty("isPublic")
    private boolean isPublic;

    @JsonProperty("isStar")
    private boolean isStar;
    private String tags;
}
