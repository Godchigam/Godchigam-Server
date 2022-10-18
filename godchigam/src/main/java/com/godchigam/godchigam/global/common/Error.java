package com.godchigam.godchigam.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Error {
    private String code;
    private String message;

    public static Error of(ErrorCode errorCode) {
        return new Error(errorCode.getCode(), errorCode.getMessage());
    }

    public static Error of(ErrorCode errorCode, String message) {
        return new Error(errorCode.getCode(), message);
    }
}