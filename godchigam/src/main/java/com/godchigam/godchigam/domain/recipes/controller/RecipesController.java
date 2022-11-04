package com.godchigam.godchigam.domain.recipes.controller;

import com.godchigam.godchigam.domain.recipes.dto.RecipesFindResponse;
import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.recipes.repository.recipesRepository;
import com.godchigam.godchigam.domain.recipes.service.recipesService;
import com.godchigam.godchigam.domain.recipesBookmark.dto.RecipeInfoResponseDto;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/recipe")
@RequiredArgsConstructor
public class RecipesController {
    private final recipesService recipesService;
    private final JwtTokenProvider jwtTokenProvider;
    private final recipesRepository recipesRepository;

    //레시피 전체조회, 카테고리별 조회
    @GetMapping("")
    public CommonResponse<List<RecipeInfoResponseDto>> searchRecipes(@RequestParam(required = false) String theme,
                                                                     @RequestHeader("Authorization") String accessToken,
                                                                     @RequestParam(required=false) Integer filter)  {
        String userId = jwtTokenProvider.getUserLoginId(accessToken);

        //기존 전체보기 - theme안받아오는 경우
        if(theme==null&&filter==null){
            return CommonResponse.success(recipesService.readRecipes(userId),"레시피 백과 조회 성공");

        }
        if(theme!=null && filter==null){
            return CommonResponse.success(recipesService.searchRecipes(theme),"레시피 백과 조회 성공");
        }
        else if(theme==null && filter==1){ //전체보기 + 난이도 낮은 순
            return CommonResponse.success(recipesService.readAllDifficulty(userId),"레시피 백과 조회 성공");
        }
        else if(theme!=null && filter==1){ //카테고리 보기 + 난이도 낮은 순
            return CommonResponse.success(recipesService.readCategoryDifficulty(userId,theme),"레시피 백과 조회 성공");
        }
        else if(theme==null && filter==2){ //전체보기 + 시간 적은 순
            return CommonResponse.success(recipesService.readAllTime(userId),"레시피 백과 조회 성공");
        }
        else if(theme!=null && filter==2){ //카테고리 보기 + 시간 적은 순
            return CommonResponse.success(recipesService.readCategoryTime(userId,theme),"레시피 백과 조회 성공");
        }
        else if(theme==null && filter==0){ //전체보기 + 좋아요 순
            return CommonResponse.success(recipesService.readAllLikes(userId),"레시피 백과 조회 성공");
        }
        else//카테고리별 보기 + 좋아요 순
            return CommonResponse.success(recipesService.readCategoryLikes(theme),"레시피 백과 조회 성공");
        }



    @GetMapping("/detail")
    public CommonResponse readRecipes(@RequestParam(required = true)Long recipeId){
        Optional<Recipes> recipes = recipesRepository.findById(recipeId);
        if(recipes.isEmpty()){
            return CommonResponse.error(ErrorCode.RECIPES_EMPTY.getStatus(), ErrorCode.RECIPES_EMPTY.getMessage());
        }
        return CommonResponse.success(recipesService.readRecipe(recipeId),"레시피 상세 조회 성공");
    }




}
