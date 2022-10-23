package com.godchigam.godchigam.domain.refrigerator.dto;

import com.godchigam.godchigam.global.dto.DateInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class FoodInfoResponse {
    private final Long foodId;
    private final String foodName;
    private final DateInfoResponse expirationDate;
    private final DateInfoResponse purchaseDate;
    private final Integer amount;
    private final Integer remain;
    private final String storage;
}
