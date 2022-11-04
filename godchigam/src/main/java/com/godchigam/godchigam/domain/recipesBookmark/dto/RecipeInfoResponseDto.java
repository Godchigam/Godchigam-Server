package com.godchigam.godchigam.domain.recipesBookmark.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class RecipeInfoResponseDto {
     private final Long recipeId;
     private final String recipeImageUrl;
     private final String recipeTitle;

     private final String dishes;
     private final String time;
     private final String difficulty;
     private final Integer likeCount;
     private final Boolean isBookmarked;
     private final Boolean isLiked;

}

