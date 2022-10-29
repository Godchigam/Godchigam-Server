package com.godchigam.godchigam.domain.recipes.controller;

import com.godchigam.godchigam.domain.recipes.service.recipesService;
import com.godchigam.godchigam.global.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import com.godchigam.godchigam.global.common.CommonResponse;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/recipes")
@RequiredArgsConstructor
public class RecipesController {
    private final recipesService recipesService;
    //레시피 전체 조회
    @GetMapping("")
    public CommonResponse readRecipes(@RequestParam(required = false)Long recipesId){
        if(recipesId==null){
            return CommonResponse.success(recipesService.readRecipes(),"레시피 전체 조회 성공");
        }

        return null;
    }
}
