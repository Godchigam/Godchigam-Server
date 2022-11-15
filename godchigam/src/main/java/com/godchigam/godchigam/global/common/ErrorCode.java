package com.godchigam.godchigam.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, 200, "요청에 성공하였습니다."),


    /**
     * 400 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 400, "입력값을 확인해주세요."),
    EMPTY_JWT(false, 400, "JWT를 입력해주세요."),
    INVALID_ACCESS_TOKEN(false,400,"유효하지 않은 JWT토큰입니다."),

    // users
    DUPLICATE_USER_ID(false, 400, "중복된 아이디가 있습니다."),
    USERS_EMPTY_USER_ID(false, 400, "존재하지 않는 계정입니다."),
    WRONG_PASSWORD(false,400,"비밀번호가 맞지 않습니다."),
    WRONG_ADDRESS(false, 400,"위치 정보를 확인해주세요."),
    NULL_ADDRESS(false,400,"거주하는 동 정보가 없는 회원은 글을 작성할 수 없습니다."),

    //refrigerator
    EMPTY_FOOD_ID(false,400,"존재하는 재료가 없습니다."),
    TOO_SMALL_AMOUNT(false,400,"재료 개수가 너무 적습니다."),
    TOO_MANY_AMOUNT(false,400,"재료 개수가 초과되었습니다."),

    // recipes
    RECIPES_EMPTY(false, 400, "존재하는 레시피 없음"),
    EMPTY_THEME_AND_FILTER(false,400,"테마와 필터 설정 안함"),

    //groupbuying
    EMPTY_PRODUCT_ID(false,400,"존재하는 공동구매 상품이 없습니다."),
    EMPTY_REQUEST_ID(false,400,"존재하지 않는 요청입니다."),
    TOO_LONG_PRODUCT_NAME(false, 400, "최대 16자 이내로 입력해주세요."),

    /**
     * 500 : Server 오류
     */
    SERVER_ERROR(false, 500, "서버와의 연결에 실패하였습니다.");



    private final boolean isSuccess;
    private final int status;
    private final String message;


}
