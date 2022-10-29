package com.godchigam.godchigam.domain.recipes.controller;

import com.godchigam.godchigam.domain.recipes.dto.RecipesFindResponse;
import com.godchigam.godchigam.domain.recipes.service.recipesService;
import com.godchigam.godchigam.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import com.godchigam.godchigam.global.common.CommonResponse;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/recipe")
@RequiredArgsConstructor
public class RecipesController {
    private final recipesService recipesService;
    //레시피 전체 조회

    /*
    @GetMapping("")
    public CommonResponse<List<RecipesFindResponse>> readRecipes(@Param("theme")String theme){
        return CommonResponse.success(recipesService.readRecipes(),"레시피 전체 조회 성공");
    }
*/

    @GetMapping("")
    public CommonResponse<List<RecipesFindResponse>> searchRecipes(@RequestParam(required = false) String theme){
        if(theme==null){
            return CommonResponse.success(recipesService.readRecipes(),"레시피 전체 조회 성공");
        }
        return CommonResponse.success(recipesService.searchRecipes(theme),"카테고리별 레시피 조회 성공");
    }


}
