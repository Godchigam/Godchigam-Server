package com.godchigam.godchigam.domain.recipesWish.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;

@Getter
@RequiredArgsConstructor
@Builder
public class WishResponse {
    //private final String userId;
    //private final Long recipesId;
    //private final String name;
    private final Integer likeCount;
    private final Boolean isLiked;
}
