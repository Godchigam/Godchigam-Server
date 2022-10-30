package com.godchigam.godchigam.domain.recipesBookmark.dto;

import com.godchigam.godchigam.domain.recipesBookmark.model.BookmarkStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class BookmarkResponse {
    private final String userId;
    private final Long recipesId;
    private final String name;
    private final BookmarkStatus status;
}
