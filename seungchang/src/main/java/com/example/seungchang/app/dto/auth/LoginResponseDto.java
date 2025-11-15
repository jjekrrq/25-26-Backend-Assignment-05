package com.example.seungchang.app.dto.auth;

import com.example.seungchang.app.domain.auth.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String accessToken;
    private String refreshToken;
    private Long id;
    private String email;
    private String name;
    private String role;
    private AuthProvider authProvider;
}
