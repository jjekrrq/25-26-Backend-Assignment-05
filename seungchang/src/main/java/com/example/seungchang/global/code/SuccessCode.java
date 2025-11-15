package com.example.seungchang.global.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {
    // ✅ Auth
    USER_SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공"), // 201
    USER_LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"), // 200
    USER_LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),
    TOKEN_REFRESH_SUCCESS(HttpStatus.OK, "Access Token 재발급 성공"), // 200

    // ✅ Restaurant
    RESTAURANT_FOUND(HttpStatus.OK, "식당 조회 성공"),
    RESTAURANT_UPDATED(HttpStatus.OK, "식당 수정 성공"),
    RESTAURANT_LIST_FOUND(HttpStatus.OK, "전체 식당 조회 성공"),
    RESTAURANT_CREATED(HttpStatus.CREATED, "식당 생성 성공"),
    RESTAURANT_DELETED(HttpStatus.NO_CONTENT, "식당 삭제 성공"),

    // ✅ Food
    FOOD_FOUND(HttpStatus.OK, "음식 조회 성공"),
    FOOD_UPDATED(HttpStatus.OK, "음식 수정 성공"),
    FOOD_SOLD(HttpStatus.OK, "음식 판매 성공"),
    FOOD_CREATED(HttpStatus.CREATED, "음식 생성 성공"),
    FOOD_DELETED(HttpStatus.NO_CONTENT, "음식 삭제 성공");

    private final HttpStatus httpStatus;
    private final String message;
}
