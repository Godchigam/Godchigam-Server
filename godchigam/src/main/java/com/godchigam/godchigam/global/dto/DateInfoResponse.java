package com.godchigam.godchigam.global.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder
public class DateInfoResponse {
    private final Integer year;
    private final Integer month;
    private final Integer day;
}
