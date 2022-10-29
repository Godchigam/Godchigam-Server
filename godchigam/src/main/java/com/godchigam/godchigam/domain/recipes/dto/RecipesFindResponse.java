package com.godchigam.godchigam.domain.recipes.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;

@Getter
@RequiredArgsConstructor
@Builder
public class RecipesFindResponse {
    private final String id;
    private final String name;
    private final String image_url;
    private final String category;
    private final String site_id;
    private final String link;
    private final String dish;
    private final String cooking_time;
    private final String difficulty;
    private final String ingredient;
    private final String cooking_method;
}
