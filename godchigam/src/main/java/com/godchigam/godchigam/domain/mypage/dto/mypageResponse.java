package com.godchigam.godchigam.domain.mypage.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Column;

@Getter
@RequiredArgsConstructor
@Builder
public class mypageResponse {
    private final String nickname;
    private final String profileImageUrl;
    private final String address;

}
