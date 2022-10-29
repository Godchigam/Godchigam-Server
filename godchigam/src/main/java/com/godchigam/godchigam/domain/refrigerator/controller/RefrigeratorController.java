package com.godchigam.godchigam.domain.refrigerator.controller;

import com.godchigam.godchigam.domain.refrigerator.dto.ChangeAmountRequest;
import com.godchigam.godchigam.domain.refrigerator.dto.NewIngredientRequest;
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

    @GetMapping("")
    public CommonResponse LookUpRefrigerator(@RequestHeader("token") String accessToken) {
        return CommonResponse.success(refrigeratorService.LookUpRefrigerator(jwtTokenProvider.getUserLoginId(accessToken)), "냉장고 조회 성공");
    }

    @GetMapping("/detail")
    public CommonResponse LookUpDetailIngredientInfo(@RequestParam Long foodId) {
        return CommonResponse.success(refrigeratorService.LookUpDetailIngredientInfo(foodId), "재료 상세 정보 조회 성공");
    }

    @PostMapping("")
    public CommonResponse addNewIngredient(@RequestHeader("token") String accessToken, @RequestBody NewIngredientRequest newIngredientRequest) {
        refrigeratorService.addNewIngredient(jwtTokenProvider.getUserLoginId(accessToken), newIngredientRequest);
        return CommonResponse.successWithOutData("재료 추가 성공");
    }

    @PutMapping("")
    public CommonResponse addIngredientAmount(@RequestHeader("token") String accessToken, @RequestBody ChangeAmountRequest changeAmount, @RequestParam Long foodId) {
        int result = refrigeratorService.addIngredientAmount(changeAmount.getChangeAmount(), foodId);
        if (result <= 0) {
            return CommonResponse.error(400, "재료 개수가 너무 적습니다.");
        } else if (result >= 99) {
            return CommonResponse.error(400, "재료 개수가 초과되었습니다.");
        }
        return CommonResponse.success(result, "재료 개수 변경 성공");
    }

    @DeleteMapping("")
    public CommonResponse deleteIngredient(@RequestHeader("token") String accessToken, @RequestParam Long foodId) {
        refrigeratorService.deleteIngredient(foodId);
        return CommonResponse.successWithOutData("재료 삭제 성공");
    }

    @PostMapping("/info")
    public CommonResponse changeIngredientDetail(@RequestHeader("token") String accessToken, @RequestBody NewIngredientRequest changeIngredientRequest, @RequestParam Long foodId){
        refrigeratorService.changeIngredientDetail(changeIngredientRequest, foodId);
        return CommonResponse.successWithOutData("재료 정보 수정 성공");
    }
}
