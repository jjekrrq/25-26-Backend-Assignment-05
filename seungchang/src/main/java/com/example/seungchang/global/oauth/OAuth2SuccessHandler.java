package com.example.seungchang.global.oauth;

import com.example.seungchang.app.domain.auth.AuthProvider;
import com.example.seungchang.app.dto.auth.LoginResponseDto;
import com.example.seungchang.app.service.RefreshTokenService;
import com.example.seungchang.global.code.SuccessCode;
import com.example.seungchang.global.jwt.TokenProvider;
import com.example.seungchang.global.responseTemplate.ApiResponseTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // GoogleOAuth2UserService 에서 넣어준 값들
        Long userId = ((Number) oAuth2User.getAttribute("userId")).longValue();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String roleName = (String) oAuth2User.getAttribute("role"); // "USER" or "ADMIN"
        AuthProvider authProvider =  oAuth2User.getAttribute("provider");

        // ✅ JWT 발급 (기존 로그인 로직과 동일한 형태)
        String accessToken = tokenProvider.createAccessToken(userId, roleName);
        String refreshToken = tokenProvider.createRefreshToken(userId);

        // ✅ DB에 RefreshToken 저장/갱신
        refreshTokenService.saveOrUpdate(userId, refreshToken);

        // ✅ 기존 LoginResponseDto 재사용
        LoginResponseDto loginResponse = LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(userId)
                .email(email)
                .name(name)
                .role("ROLE_" + roleName) // 기존 로그인 응답과 포맷 맞춤
                .authProvider(authProvider)
                .build();

        // ✅ ApiResponseTemplate 형식에 맞춰 JSON 응답
        ApiResponseTemplate<LoginResponseDto> body =
                ApiResponseTemplate.<LoginResponseDto>builder()
                        .status(SuccessCode.USER_LOGIN_SUCCESS.getHttpStatus().value())
                        .success(true)
                        .message(SuccessCode.USER_LOGIN_SUCCESS.getMessage())
                        .data(loginResponse)
                        .build();

        response.setStatus(SuccessCode.USER_LOGIN_SUCCESS.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
