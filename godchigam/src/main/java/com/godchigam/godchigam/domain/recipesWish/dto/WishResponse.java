package com.godchigam.godchigam.domain.recipesWish.dto;

import com.godchigam.godchigam.domain.recipesWish.model.WishStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class WishResponse {
    private final String userId;
    private final Long recipesId;
    private final String name;
    private final WishStatus status;
}
