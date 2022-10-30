package com.godchigam.godchigam.global.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthRequest {
    private String loginId;
    private String password;
    private String nickname;
    private String profileImageUrl;
    private String address;
}
