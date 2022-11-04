package com.godchigam.godchigam.domain.mypage.controller;

import com.godchigam.godchigam.domain.mypage.service.mypageService;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
public class mypageController {
    private final JwtTokenProvider jwtTokenProvider;
    private final mypageService mypageService;

    @GetMapping("")
    public CommonResponse readMypage(@RequestHeader("Authorization") String accessToken){

        String userId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(mypageService.checkMypage(userId),"메인 정보 조회 성공");
    }
}
