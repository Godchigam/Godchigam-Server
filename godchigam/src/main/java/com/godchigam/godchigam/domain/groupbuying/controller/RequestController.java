package com.godchigam.godchigam.domain.groupbuying.controller;

import com.godchigam.godchigam.domain.groupbuying.service.RequestService;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groupbuying")
public class RequestController {

    private final JwtTokenProvider jwtTokenProvider;
    private final RequestService requestService;

    @GetMapping("/request")
    public CommonResponse LookUpRequestStorage(@RequestHeader("token") String accessToken) {
        String loginId= jwtTokenProvider.getUserLoginId(accessToken);
        return CommonResponse.success(requestService.LookUpRequestStorage(loginId),"같이 구매 요청함 조회 성공");
    }
}
