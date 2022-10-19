package com.godchigam.godchigam.global.auth;

import com.godchigam.godchigam.global.auth.dto.AuthRequest;
import com.godchigam.godchigam.global.auth.dto.LoginRequest;
import com.godchigam.godchigam.global.auth.dto.UserResponse;
import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/check")
    public CommonResponse UserIdDuplicateCheck(@RequestParam String loginId) {
        String message = authService.UserIdDuplicateCheck(loginId);
        if(message == null)
            return CommonResponse.error(ErrorCode.DUPLICATE_USER_ID.getStatus(),ErrorCode.DUPLICATE_USER_ID.getMessage());
        return CommonResponse.successWithOutData(message);
    }

    @PostMapping("/signup")
    public CommonResponse SignUpUser(@RequestBody AuthRequest authRequest) {
        String message =authService.SignUpUser(authRequest);
        return CommonResponse.successWithOutData(message);
    }

    @PostMapping("/login")
    public CommonResponse UserLogin(@RequestBody LoginRequest loginRequest){
        return authService.UserLogin(loginRequest);
    }
    }
}
