package com.godchigam.godchigam.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Error {
    private int status;
    private String message;

    public static Error of(ErrorCode errorCode) {
        return new Error(errorCode.getStatus(), errorCode.getMessage());
    }

    public static Error of(ErrorCode errorCode, String message) {
        return new Error(errorCode.getStatus(), message);
    }
}