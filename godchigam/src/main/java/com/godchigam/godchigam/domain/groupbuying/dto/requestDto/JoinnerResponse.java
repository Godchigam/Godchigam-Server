package com.godchigam.godchigam.domain.groupbuying.dto.requestDto;

import com.godchigam.godchigam.domain.groupbuying.dto.ProductInfo;
import com.godchigam.godchigam.domain.groupbuying.dto.UserInfoResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class JoinnerResponse {
    private final Boolean isOwner;
    private final List<JoinPeopleResponse> joinPeople;
}
