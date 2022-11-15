package com.godchigam.godchigam.domain.groupbuying.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class DetailProductInfoResponse {
    private final boolean ifOwner;
    private final boolean isFull;
    private final String joinType;
    private final String address;
    private final String category;
    private final ProductInfo productInfo;
    private final UserInfoResponse owner;
    private final List<UserInfoResponse> joinPeople;
    private final String description;
}
