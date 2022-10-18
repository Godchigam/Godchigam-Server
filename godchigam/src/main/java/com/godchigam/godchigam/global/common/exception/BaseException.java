package com.godchigam.godchigam.global.common.exception;

import com.godchigam.godchigam.global.common.ErrorCode;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException{
    private ErrorCode errorCode;
    public BaseException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode=errorCode;
    }
    public ErrorCode getErrorCode(){
        return this.errorCode;
    }
}