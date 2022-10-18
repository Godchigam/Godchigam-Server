package com.godchigam.godchigam.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {

    private int status;
    private boolean success;
    private T data;
    private Error error;

    public static <T> CommonResponse<T> error(ErrorCode errorCode) {
        return new CommonResponse<>(400,false , null, Error.of(errorCode));
    }

    public static <T> CommonResponse<T> error(ErrorCode errorCode, String message) {
        return new CommonResponse<>(400,false , null, Error.of(errorCode, message));
    }
}
