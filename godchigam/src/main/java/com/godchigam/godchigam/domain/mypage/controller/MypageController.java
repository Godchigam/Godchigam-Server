package com.godchigam.godchigam.domain.mypage.controller;

import com.godchigam.godchigam.domain.mypage.dto.MypageRequest;
import com.godchigam.godchigam.domain.mypage.dto.MypageResponse;
import com.godchigam.godchigam.domain.mypage.service.MypageService;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/my")
public class MypageController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MypageService mypageService;

    @GetMapping("")
    public CommonResponse readMypage(@RequestHeader("token") String accessToken){

        String userId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(mypageService.checkMypage(userId),"메인 정보 조회 성공");
    }

    @PostMapping("")
    public CommonResponse<MypageResponse> updateUser(@RequestHeader("token") String accessToken, @RequestBody MypageRequest request){
        String userId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(mypageService.updateMypage(userId, request),"개인정보 수정 성공");
    }

}
