package com.godchigam.godchigam.domain.refrigerator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class AddIngredientResponse {
    private final Long foodId;
}
