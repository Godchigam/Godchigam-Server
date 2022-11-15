package com.godchigam.godchigam.domain.groupbuying.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class DetailProductInfoResponse {
    private final Boolean isOwner;
    private final Boolean isFull;
    private final String joinType;
    private final String address;
    private final String category;
    private final ProductInfo productInfo;
    private final UserInfoResponse owner;
    private final List<UserInfoResponse> joinPeople;
    private final String description;
}
