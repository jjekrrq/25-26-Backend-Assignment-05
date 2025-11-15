package com.example.seungchang.app.controller;

import com.example.seungchang.app.dto.auth.*;
import com.example.seungchang.app.service.UserService;
import com.example.seungchang.global.code.SuccessCode;
import com.example.seungchang.global.responseTemplate.ApiResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponseTemplate<Void>> signUp(@RequestBody @Valid UserSignUpRequestDto dto) {
        userService.signUp(dto);
        return ApiResponseTemplate.success(SuccessCode.USER_SIGNUP_SUCCESS, null);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseTemplate<LoginResponseDto>> login(@RequestBody @Valid LoginRequestDto dto) {
        LoginResponseDto response = userService.login(dto);
        return ApiResponseTemplate.success(SuccessCode.USER_LOGIN_SUCCESS, response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseTemplate<TokenDto>> refresh(@RequestBody RefreshTokenRequestDto request) {
        TokenDto tokenDto = userService.refresh(request);
        return ApiResponseTemplate.success(SuccessCode.TOKEN_REFRESH_SUCCESS, tokenDto);
    }

    //  로그아웃
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseTemplate<Void>> logout(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = Long.parseLong(userDetails.getUsername());
        userService.logout(userId);
        return ApiResponseTemplate.success(SuccessCode.USER_LOGOUT_SUCCESS, null);
    }
}
