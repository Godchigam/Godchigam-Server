package com.godchigam.godchigam.domain.recipesWish.controller;

import com.godchigam.godchigam.domain.recipesBookmark.dto.BookmarkResponse;
import com.godchigam.godchigam.domain.recipesWish.dto.WishResponse;
import com.godchigam.godchigam.domain.recipesWish.service.WishService;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class WishController {
    private final JwtTokenProvider jwtTokenProvider;
    private final WishService wishService;


    /**
     * 어떤 유저가 한 테마에 찜하기 누르기
     * **/

    @PutMapping("/like")
    public CommonResponse<WishResponse> CheckWish(@RequestHeader("Authorization") String accessToken, @RequestParam Long recipeId){
        //jwt복호화, user정보 얻기
        String userId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(wishService.checkWish(userId,recipeId),"찜하기 체크 성공");
    }
}
