package com.example.seungchang.app.domain.auth;

import com.example.seungchang.app.dto.auth.UserSignUpRequestDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 120)
    private String password;

    @Column(nullable = false, length = 60)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "USER_ROLE", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Column(nullable = false)
    private String providerId;

    // ✅ 구글/카카오/네이버 같은 소셜 계정용 유저 생성
    public static User createSocialUser(
            String email,
            String name,
            AuthProvider provider,
            String providerId
    ) {
        return User.builder()
                .email(email)
                // 실제로 로그인에 비밀번호를 쓰지 않으므로 더미 값
                .password("SOCIAL_LOGIN_USER")
                .name(name)
                .role(Role.USER) // 기본 ROLE_USER
                .provider(provider)
                .providerId(providerId)
                .build();
    }

    // 일반 로그인 정적 팩토리 메서드
    public static User createNotSocialUser(
            String email,
            String name,
            String encodedPassword  // 이미 인코딩된 비밀번호를 받음
    ){
        return User.builder()
                .email(email)
                .password(encodedPassword)  // 암호화된 값 넣기
                .name(name)
                .role(Role.USER)
                .provider(AuthProvider.LOCAL)
                .providerId(email)
                .build();
    }

}
