package com.example.seungchang.global.exception.handler;

import com.example.seungchang.global.code.ErrorCode;
import com.example.seungchang.global.exception.*;
import com.example.seungchang.global.responseTemplate.ApiResponseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseTemplate<Void>> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("Validation error: {}", e.getMessage());
        return ApiResponseTemplate.error(ErrorCode.VALIDATION_EXCEPTION);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseTemplate<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("Illegal argument: {}", e.getMessage());
        return ApiResponseTemplate.error(ErrorCode.VALIDATION_EXCEPTION);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ApiResponseTemplate<Void>> handleRestaurantNotFound(RestaurantNotFoundException e) {
        log.warn("Restaurant not found: {}", e.getMessage());
        return ApiResponseTemplate.error(ErrorCode.NOT_FOUND_RESTAURANT_EXCEPTION);
    }

    @ExceptionHandler(FoodNotFoundException.class)
    public ResponseEntity<ApiResponseTemplate<Void>> handleFoodNotFound(FoodNotFoundException e) {
        log.warn("Food not found: {}", e.getMessage());
        return ApiResponseTemplate.error(ErrorCode.NOT_FOUND_RESTAURANT_EXCEPTION);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponseTemplate<Void>> handleAuthException(AuthException e) {
        log.warn("Auth error: {}", e.getMessage());
        return ApiResponseTemplate.error(e.getErrorCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseTemplate<Void>> handleException(Exception e) {
        log.error("Unexpected error", e);
        return ApiResponseTemplate.error(ErrorCode.INTERNAL_SERVER_EXCEPTION);
    }
}
