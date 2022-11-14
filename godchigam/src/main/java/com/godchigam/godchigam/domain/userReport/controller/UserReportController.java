package com.godchigam.godchigam.domain.userReport.controller;

import com.godchigam.godchigam.domain.user.entity.User;
import com.godchigam.godchigam.domain.userReport.dto.UserReportCreationRequest;
import com.godchigam.godchigam.domain.userReport.model.UserReport;
import com.godchigam.godchigam.domain.userReport.service.UserReportService;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping(value = "/community")
@RequiredArgsConstructor
public class UserReportController {
    private final UserReportService userReportService;
    private final JwtTokenProvider jwtTokenProvider;

    //
    @PostMapping("/report")
    public CommonResponse<UserReport> createReport(@RequestHeader("token") String accessToken, @RequestBody UserReportCreationRequest request){
        String userId = jwtTokenProvider.getUserLoginId(accessToken);
        UserReport report = userReportService.createReport(userId,request);
        if(report == null){
            //에러발생
            return CommonResponse.error(ErrorCode.USERS_EMPTY_USER_ID.getStatus(), ErrorCode.USERS_EMPTY_USER_ID.getMessage());
        }
        return CommonResponse.success(null,"유저 신고 성공");

    }
}