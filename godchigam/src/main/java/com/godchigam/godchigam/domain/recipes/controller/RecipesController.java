package com.godchigam.godchigam.domain.recipes.controller;

import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.recipes.repository.RecipesRepository;
import com.godchigam.godchigam.domain.recipes.service.recipesService;
import com.godchigam.godchigam.domain.recipesBookmark.dto.RecipeInfoResponseDto;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/recipe", produces = "application/json; charset=utf8", method = RequestMethod.GET)
@RequiredArgsConstructor
@Slf4j
public class RecipesController {
    private final recipesService recipesService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RecipesRepository recipesRepository;

    //레시피 전체조회, 카테고리별 조회
    @GetMapping("")
    public CommonResponse<List<RecipeInfoResponseDto>> searchRecipes(@RequestParam(required = false) Integer theme,
                                                                     @RequestHeader("Authorization") String accessToken,
                                                                     @RequestParam(required=false) Integer filter) throws UnsupportedEncodingException {
        String userId = jwtTokenProvider.getUserLoginId(accessToken);
        if (theme == null || filter == null) {
            throw new BaseException(ErrorCode.EMPTY_THEME_AND_FILTER);
        }

        else{

           return CommonResponse.success(recipesService.searchRecipes(theme, filter, userId),"성공");
        }
    }



    @GetMapping("/detail")
    public CommonResponse readRecipes(@RequestParam(required = true)Long recipeId, @RequestHeader("Authorization") String accessToken){
        Optional<Recipes> recipes = recipesRepository.findById(recipeId);
        String userId = jwtTokenProvider.getUserLoginId(accessToken);
        if(recipes.isEmpty()){
            return CommonResponse.error(ErrorCode.RECIPES_EMPTY.getStatus(), ErrorCode.RECIPES_EMPTY.getMessage());
        }
        return CommonResponse.success(recipesService.readRecipe(recipeId, userId),"레시피 상세 조회 성공");
    }




}
