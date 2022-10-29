package com.godchigam.godchigam.domain.recipes.service;


import com.godchigam.godchigam.domain.recipes.controller.RecipesController;
import com.godchigam.godchigam.domain.recipes.dto.RecipesFindResponse;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.recipes.repository.recipesRepository;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class recipesService {

    private final recipesRepository recipesRepository;

    //레시피 전체 조회
    @Transactional
    public List<RecipesFindResponse> readRecipes(){
        List<Recipes> recipes = recipesRepository.findAll();
        if(recipes.isEmpty()){
            throw new BaseException(ErrorCode.RECIPES_EMPTY);
        }
        recipes = recipes.stream().distinct().collect(Collectors.toList());
        List<RecipesFindResponse> newList = new ArrayList<>();
        recipes.forEach(recipes1 -> {
                    newList.add(
                            RecipesFindResponse.builder()
                                    .id(recipes1.getId())
                                    .name(recipes1.getName())
                                    .image_url(recipes1.getImage_url())
                                    .category(recipes1.getCategory())
                                    .site_id(recipes1.getSite_id())
                                    .link(recipes1.getLink())
                                    .dish(recipes1.getDish())
                                    .cooking_time(recipes1.getCooking_time())
                                    .difficulty(recipes1.getDifficulty())
                                    .ingredient(recipes1.getIngredient())
                                    .cooking_method(recipes1.getCooking_method())
                                    .build()
                    );
                }
        );
        return newList;


    }

    @Transactional
    public List<RecipesFindResponse> searchRecipes(String keyword){
        List<Recipes> recipes = recipesRepository.findByCategoryContaining(keyword);
        if(recipes.isEmpty()){
            throw new BaseException(ErrorCode.RECIPES_EMPTY);
        }
        recipes = recipes.stream().distinct().collect(Collectors.toList());
        List<RecipesFindResponse> newList = new ArrayList<>();
        recipes.forEach(recipes1 -> {
                    newList.add(
                            RecipesFindResponse.builder()
                                    .id(recipes1.getId())
                                    .name(recipes1.getName())
                                    .image_url(recipes1.getImage_url())
                                    .category(recipes1.getCategory())
                                    .site_id(recipes1.getSite_id())
                                    .link(recipes1.getLink())
                                    .dish(recipes1.getDish())
                                    .cooking_time(recipes1.getCooking_time())
                                    .difficulty(recipes1.getDifficulty())
                                    .ingredient(recipes1.getIngredient())
                                    .cooking_method(recipes1.getCooking_method())
                                    .build()
                    );
                }
        );
        return newList;
    }


}
