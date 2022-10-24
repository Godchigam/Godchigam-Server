package com.godchigam.godchigam.global.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.DayOfWeek;
import java.time.Month;

@Getter
@RequiredArgsConstructor
@Builder
public class DateInfoResponse {
    private final int year;
    private final int month;
    private final int day;
}
