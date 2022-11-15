package com.godchigam.godchigam.domain.groupbuying.dto.requestDto;

import com.godchigam.godchigam.domain.groupbuying.dto.UserInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class RequestMessageResponse {
    private final UserInfoResponse requesterInfo;
    private final Long requestId;
    private final String productName;
    private final Long productId;
    private final String requestType;
}
