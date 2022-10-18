package com.godchigam.godchigam.global.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthRequest {
    private String loginId;
    private String password;
    private String nickname;
    private String profileImageUrl;
}
