package com.godchigam.godchigam.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {

    private int status;
    private boolean success;
    private String message;
    private T data;

    public static <T> CommonResponse<T> successWithOutData(String message) {
        return new CommonResponse<>(200, true, message, null);
    }

    public static <T> CommonResponse<T> success(T data,String message) {
        return new CommonResponse<>(200,true,message,data);
    }

    public static <T> CommonResponse<T> error(int status,String message) {
        return new CommonResponse<>(status,false , message, null);
    }
}
