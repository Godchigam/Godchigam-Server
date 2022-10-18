package com.godchigam.godchigam.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonSuccessResponse<T> {

    private int status;
    private boolean success;
    private String message;
    private T data;

    public static <T> CommonSuccessResponse<T> successWithOutData(String message) {
        return new CommonSuccessResponse<>(200, true, message, null);
    }

    public static <T> CommonSuccessResponse<T> success(T data,String message) {
        return new CommonSuccessResponse<>(200,true,message,data);
    }

}
