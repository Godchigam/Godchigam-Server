package com.godchigam.godchigam.domain.recipes.dto;
import com.godchigam.godchigam.domain.recipesBookmark.dto.RecipeInfoResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class RecipeDetailResponse {
    private final RecipeInfoResponseDto recipeInfo;
    private final List<String> ingredients;
    private final List<String> orders;

}
