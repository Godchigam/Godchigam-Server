package com.godchigam.godchigam.domain.groupbuying.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CheckRequest {
    private Long requestId;
    private Long productId;
    private Long userId;
    private String requestType;
}
