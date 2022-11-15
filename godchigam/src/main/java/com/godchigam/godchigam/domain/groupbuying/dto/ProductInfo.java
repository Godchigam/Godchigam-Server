package com.godchigam.godchigam.domain.groupbuying.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@Setter
@Builder
public class ProductInfo {
    private final Long productId;
    private final String productImageUrl;
    private final String purchaseStatus;
    private final String productName;
    private final Integer productPrice;
    private final Integer dividedPrice;
    private final Integer goalPeopleCount;
    private final Integer joinPeopleCount;
    private final String dealingMethod;
}
