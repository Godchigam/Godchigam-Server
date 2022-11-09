package com.godchigam.godchigam.global.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private boolean isSuccess;
    private int status;
    private String message;

    public ErrorResponse(ErrorCode errorCode){
        this.isSuccess = errorCode.isSuccess();
        this.message = errorCode.getMessage();
        this.status = errorCode.getStatus();
    }
}
