package com.godchigam.godchigam.global.auth;

import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.CommonSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/check")
    public CommonSuccessResponse UserIdDuplicateCheck(@RequestParam String loginId) {
        return CommonSuccessResponse.successWithOutData(authService.UserIdDuplicateCheck(loginId));
    }

    @PostMapping("/signup")
    public CommonSuccessResponse SignUpUser(@RequestBody AuthRequest authRequest) {
        return CommonSuccessResponse.successWithOutData(authService.SignUpUser(authRequest));
    }
}
