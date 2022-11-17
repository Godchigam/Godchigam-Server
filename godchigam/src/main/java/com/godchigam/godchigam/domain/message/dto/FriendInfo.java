package com.godchigam.godchigam.domain.message.dto;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class FriendInfo {
    private final Long userId;
    private final String profileImageUrl;
    private final String nickname;
}
