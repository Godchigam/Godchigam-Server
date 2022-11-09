package com.godchigam.godchigam.global.common.exception;

import com.godchigam.godchigam.global.common.CommonResponse;
import com.godchigam.godchigam.global.common.ErrorCode;
import com.godchigam.godchigam.global.common.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(WebClientException.class)
    private ResponseEntity<CommonResponse> handleInternalServerError(WebClientException e){
        log.warn(e.getMessage(), e);
        CommonResponse errorResponse = CommonResponse.error(400,e.getMessage());
        return new ResponseEntity<CommonResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<CommonResponse> BaseException(BaseException e){
        log.error(e.getMessage(),e);
        CommonResponse errorResponse = CommonResponse.error(e.getErrorCode().getStatus(),e.getMessage());
        return new ResponseEntity<CommonResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}