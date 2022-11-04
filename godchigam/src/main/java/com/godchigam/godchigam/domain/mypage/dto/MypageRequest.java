package com.godchigam.godchigam.domain.mypage.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 프론트에서 넘겨주는 accesstoken. ex) 카카오에서 부여받은 access token. 서비스내에서 발급한 jwt token아님
 */
@Getter
@NoArgsConstructor

public class MypageRequest {
    private String nickname;
    private String profileImageUrl;
    private String address;
}
