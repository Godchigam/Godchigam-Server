package com.godchigam.godchigam.domain.recipesWish.controller;

import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.recipes.repository.RecipesRepository;
import com.godchigam.godchigam.domain.recipesWish.dto.WishResponse;
import com.godchigam.godchigam.domain.recipesWish.service.WishService;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class WishController {
    private final JwtTokenProvider jwtTokenProvider;
    private final WishService wishService;
    private final RecipesRepository recipesRepository;

    /**
     * 어떤 유저가 한 테마에 찜하기 누르기
     * **/

    @PutMapping("/like")
    public CommonResponse<WishResponse> CheckWish(@RequestHeader("token") String accessToken, @RequestParam Long recipeId){
        //jwt복호화, user정보 얻기
        String userId = jwtTokenProvider.getUserLoginId(accessToken);
        Optional<Recipes> recipes = recipesRepository.findById(recipeId);
        if(recipes.isEmpty()){
            return CommonResponse.error(ErrorCode.RECIPES_EMPTY.getStatus(), ErrorCode.RECIPES_EMPTY.getMessage());
        }
        return CommonResponse.success(wishService.checkWish(userId,recipeId),"레시피 좋아요 및 취소 성공");
    }
}
