package com.godchigam.godchigam.domain.groupbuying.dto.groupbuyingDto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class GroupBuyingMainResponse {
    private final Integer reportCount;
    private final String address;

}
