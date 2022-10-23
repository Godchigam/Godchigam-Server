package com.godchigam.godchigam.global.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class UserResponse {
    private final String nickname;
    private final String accessToken;
}
