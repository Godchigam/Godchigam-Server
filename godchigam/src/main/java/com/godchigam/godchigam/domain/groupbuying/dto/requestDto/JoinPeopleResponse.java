package com.godchigam.godchigam.domain.groupbuying.dto.requestDto;

import com.godchigam.godchigam.domain.groupbuying.dto.UserInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class JoinPeopleResponse {
    private final Boolean isMe;
    private final UserInfoResponse userInfo;
}
