package com.godchigam.godchigam.domain.recipesBookmark.controller;

import com.godchigam.godchigam.domain.recipesBookmark.dto.BookmarkResponse;
import com.godchigam.godchigam.domain.recipesBookmark.model.BookmarkStatus;
import com.godchigam.godchigam.domain.recipesBookmark.service.BookmarkService;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipe")
public class BookmarkController {
    private final JwtTokenProvider jwtTokenProvider;
    private final BookmarkService bookmarkService;

    /**
     * 어떤 유저가 한 테마에 북마크 누르기
     * **/

    @PutMapping("/bookmark")
    public CommonResponse<BookmarkResponse> CheckBookmark(@RequestHeader("Authorization") String accessToken, @RequestParam Long recipeId){

       String userId = jwtTokenProvider.getUserLoginId(accessToken);
       return CommonResponse.success(bookmarkService.checkBookmark(userId,recipeId),"북마크 체크 성공");
    }

    @GetMapping("/bookmark")
    public CommonResponse readBookmarks(@RequestHeader("Authorization") String accessToken, @RequestParam(required = false)BookmarkStatus status){
        String userId = jwtTokenProvider.getUserLoginId(accessToken);
        BookmarkStatus status1 = BookmarkStatus.ON;
        return CommonResponse.success(bookmarkService.readBookmark(userId,status1),"북마크 불러오기 성공");
    }



}
