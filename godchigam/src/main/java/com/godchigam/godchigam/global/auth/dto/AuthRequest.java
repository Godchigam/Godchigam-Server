package com.godchigam.godchigam.global.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthRequest {
    private String loginId;
    private String password;
    private String nickname;
    private String profileImageUrl;
    private String address;
}
