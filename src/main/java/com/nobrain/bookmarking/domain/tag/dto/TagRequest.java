package com.nobrain.bookmarking.domain.tag.dto;

import lombok.Getter;

import java.util.List;

public class TagRequest {

    @Getter
    public static class Selection {
        List<Long> tagIds;
    }
}
