package com.godchigam.godchigam.domain.refrigerator.controller;

import com.godchigam.godchigam.domain.refrigerator.service.RefrigeratorService;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/refrigerator")
public class RefrigeratorController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefrigeratorService refrigeratorService;

    /**
     * TO-DO : 유통기한, 산 날짜, REMAIN 관련해서 아직 남음
     * @param accessToken
     * @return
     */
    @GetMapping("")
    public CommonResponse LookUpRefrigerator(@RequestHeader("token") String accessToken) {
        return CommonResponse.success(refrigeratorService.LookUpRefrigerator(jwtTokenProvider.getUserLoginId(accessToken)),"냉장고 조회 성공");
    }

    @GetMapping("")
    public CommonResponse LookUpDetailIngredientInfo(@RequestParam Long foodId){
        return CommonResponse.success(refrigeratorService.LookUpDetailIngredientInfo(foodId),"재료 상세 정보 조회 성공");
    }
}
