package com.godchigam.godchigam.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private int status;

    private boolean success;

    private T data;

    private Error error;

    public <T> CommonResponse(int status, boolean success, T data) {
    }


    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(200, true, data);
    }


    public static <T> CommonResponse<T> error(ErrorCode errorCode, String message) {
        return new CommonResponse<>(400,false ,null, Error.of(errorCode, message));
    }
}
