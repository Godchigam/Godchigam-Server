package com.godchigam.godchigam.domain.mypage.dto;
import lombok.Builder;
import lombok.Getter;

import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
@Builder

public class MypageResponse {
    private final String nickname;
    private final String profileImageUrl;
    private final String address;

}
