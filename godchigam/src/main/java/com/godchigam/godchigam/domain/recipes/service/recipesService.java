package com.godchigam.godchigam.domain.recipes.service;


import com.godchigam.godchigam.domain.recipes.dto.RecipesFindResponse;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.recipes.repository.recipesRepository;
import com.godchigam.godchigam.domain.recipesBookmark.model.Bookmark;
import com.godchigam.godchigam.domain.recipesBookmark.model.BookmarkStatus;
import com.godchigam.godchigam.domain.recipesWish.model.WishStatus;
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
                                    .likeCount(recipes1.getCountOfLikes())


                                    .build()
                    );
                }
        );
        return newList;


    }

    //레시피 카테고리 조회
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
                                    .likeCount(recipes1.getCountOfLikes())

                                    .build()
                    );
                }
        );
        return newList;
    }

    //레시피 상세조회
    @Transactional
    public RecipesFindResponse readRecipe(Long id){
        Optional<Recipes> recipes = recipesRepository.findById(id);
        if(recipes.isEmpty()){
            throw new BaseException(ErrorCode.RECIPES_EMPTY);
        }
        return RecipesFindResponse.builder()
                .id(recipes.get().getId())
                .name(recipes.get().getName())
                .image_url(recipes.get().getImage_url())
                .category(recipes.get().getCategory())
                .site_id(recipes.get().getSite_id())
                .link(recipes.get().getLink())
                .dish(recipes.get().getDish())
                .cooking_time(recipes.get().getCooking_time())
                .difficulty(recipes.get().getDifficulty())
                .ingredient(recipes.get().getIngredient())
                .cooking_method(recipes.get().getCooking_method())
                .likeCount(recipes.get().getCountOfLikes())
                .build();
    }

}
