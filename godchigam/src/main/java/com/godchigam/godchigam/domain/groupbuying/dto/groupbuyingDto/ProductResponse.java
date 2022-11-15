package com.godchigam.godchigam.domain.groupbuying.dto.groupbuyingDto;

import com.godchigam.godchigam.domain.groupbuying.dto.ProductInfo;
import com.godchigam.godchigam.domain.refrigerator.dto.FoodInfoResponse;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class ProductResponse {
    private final Integer reportCount;
    private final String address;
    private final List<ProductInfo> food;
    private final List<ProductInfo> living;
    private final List<ProductInfo> etc;
}
