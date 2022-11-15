package com.godchigam.godchigam.domain.groupbuying.controller;

import com.godchigam.godchigam.domain.groupbuying.dto.CheckRequest;
import com.godchigam.godchigam.domain.groupbuying.dto.GroupBuyingPostRequest;
import com.godchigam.godchigam.domain.groupbuying.dto.ProductInfo;
import com.godchigam.godchigam.domain.groupbuying.dto.ProductResponse;
import com.godchigam.godchigam.domain.groupbuying.entity.Product;
import com.godchigam.godchigam.domain.groupbuying.repository.ProductRepository;
import com.godchigam.godchigam.domain.groupbuying.service.RequestService;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.exception.BaseException;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groupbuying")
public class RequestController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RequestService requestService;
    private final ProductRepository productRepository;

    @GetMapping("/requestbox")
    public CommonResponse LookUpRequestStorage(@RequestHeader("token") String accessToken) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(requestService.LookUpRequestStorage(loginId), "같이 구매 요청함 조회 성공");
    }

    @GetMapping("/join/{productId}")
    public CommonResponse checkProductJoinStatus(@RequestHeader("token") String accessToken, @PathVariable("productId") Long productId) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(requestService.checkProductJoinStatus(loginId, productId), "참여 상태 조회 성공");
    }

    @GetMapping("/joinPeople/{productId}")
    public CommonResponse checkProductJoinPeople(@RequestHeader("token") String accessToken, @PathVariable("productId") Long productId) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(requestService.checkProductJoinPeople(loginId, productId), "참여자 목록 조회 성공");
    }

    @GetMapping("/{productId}")
    public CommonResponse detailProductInfo(@RequestHeader("token") String accessToken, @PathVariable("productId") Long productId) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(requestService.detailProductInfo(loginId, productId), "상세 정보 조회 성공");
    }

    @PutMapping("/status/{productId}")
    public CommonResponse changeProductStatus(@RequestHeader("token") String accessToken, @PathVariable("productId") Long productId) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(requestService.changeProductStatus(loginId, productId), "같이 구매 상태 변경 성공");
    }

    @PostMapping("/join/request")
    public CommonResponse sendJoinRequest(@RequestHeader("token") String accessToken, @RequestBody Long productId) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(requestService.sendJoinRequest(loginId, productId), "참여/탈퇴 요청 성공");
    }

    @PostMapping("/join/cancel")
    public CommonResponse sendJoinCancel(@RequestHeader("token") String accessToken, @RequestBody Long productId) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(requestService.sendJoinCancel(loginId, productId), "참여/탈퇴 취소 요청 성공");
    }

    @PutMapping("")
    public CommonResponse checkRequest(@RequestHeader("token") String accessToken, @RequestBody CheckRequest checkRequest) {
        String loginId = jwtTokenProvider.getUserLoginId(accessToken);
        requestService.checkRequest(loginId, checkRequest);
        return CommonResponse.successWithOutData("참여/탈퇴 요청 수락 성공");
    }

}
