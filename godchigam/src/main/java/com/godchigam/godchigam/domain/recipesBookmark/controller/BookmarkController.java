package com.godchigam.godchigam.domain.recipesBookmark.controller;

import com.godchigam.godchigam.domain.recipes.entity.Recipes;
import com.godchigam.godchigam.domain.recipes.repository.RecipesRepository;
import com.godchigam.godchigam.domain.recipesBookmark.dto.BookmarkResponse;
import com.godchigam.godchigam.domain.recipesBookmark.dto.RecipeInfoResponseDto;
import com.godchigam.godchigam.domain.recipesBookmark.service.BookmarkService;
import com.godchigam.godchigam.domain.recipesWish.repository.WishRepository;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class BookmarkController {
    private final JwtTokenProvider jwtTokenProvider;
    private final BookmarkService bookmarkService;
    private final RecipesRepository recipesRepository;
    private final WishRepository wishRepository;

    //북마크 체크하기
    @PutMapping("/bookmark")
    public CommonResponse<BookmarkResponse> CheckBookmark(@RequestHeader("token") String accessToken, @RequestParam Long recipeId){

       String userId = jwtTokenProvider.getUserLoginId(accessToken);
        Optional<Recipes> recipes = recipesRepository.findById(recipeId);
        if(recipes.isEmpty()){
            return CommonResponse.error(ErrorCode.RECIPES_EMPTY.getStatus(), ErrorCode.RECIPES_EMPTY.getMessage());
        }
       return CommonResponse.success(bookmarkService.checkBookmark(userId,recipeId),"레시피 북마크 및 취소 성공");
    }


    //북마크한 레시피 불러오기
    @GetMapping("/bookmark")
    public CommonResponse<List<RecipeInfoResponseDto>> readBookmark(@RequestHeader("token") String accessToken, @RequestParam(required = false)Boolean status){
        String userId = jwtTokenProvider.getUserLoginId(accessToken);

        return CommonResponse.success(bookmarkService.readBookmark(userId,status),"북마크한 레시피 조회 성공");
    }




}
