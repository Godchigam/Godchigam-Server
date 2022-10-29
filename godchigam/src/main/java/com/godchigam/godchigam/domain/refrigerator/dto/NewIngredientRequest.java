package com.godchigam.godchigam.domain.refrigerator.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NewIngredientRequest {
    private String foodName;
    private int amount;
    private String storage;
    private int purchaseYear;
    private int purchaseMonth;
    private int purchaseDay;
    private int expirationYear;
    private int expirationMonth;
    private int expirationDay;
}
