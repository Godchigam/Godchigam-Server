package com.godchigam.godchigam.domain.groupbuying.dto.requestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class JoinStatusResponse {
    private final String joinType;
}