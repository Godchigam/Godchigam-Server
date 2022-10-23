package com.godchigam.godchigam.domain.refrigerator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class RefrigeratorResponse {
    private final List<FoodInfoResponse> freezer;
    private final List<FoodInfoResponse> cold;
    private final List<FoodInfoResponse> room;
}
