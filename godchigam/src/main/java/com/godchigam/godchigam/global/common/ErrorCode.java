package com.godchigam.godchigam.global.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, "200", "요청에 성공하였습니다."),


    /**
     * 400 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, "400", "입력값을 확인해주세요."),
    EMPTY_JWT(false, "400", "JWT를 입력해주세요."),

    // users
    DUPLICATE_USER_ID(false, "400", "중복된 아이디가 있습니다."),


    /**
     * 500 : Server 오류
     */
    SERVER_ERROR(false, "500", "서버와의 연결에 실패하였습니다.");



    private final boolean isSuccess;
    private final String status;
    private final String message;


    private ErrorCode(boolean isSuccess, String status, String message) {
        this.isSuccess = isSuccess;
        this.status = status;
        this.message = message;
    }
}
