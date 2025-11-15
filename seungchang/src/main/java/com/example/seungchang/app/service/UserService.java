package com.example.seungchang.app.service;

import com.example.seungchang.app.domain.auth.Role;
import com.example.seungchang.app.domain.auth.User;
import com.example.seungchang.app.dto.auth.*;
import com.example.seungchang.app.repository.UserRepository;
import com.example.seungchang.global.code.ErrorCode;
import com.example.seungchang.global.exception.AuthException;
import com.example.seungchang.global.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void signUp(UserSignUpRequestDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new AuthException(ErrorCode.ALREADY_EXIST_USER);
        }

        User user = User.createNotSocialUser(
                dto.getEmail(),
                dto.getName(),
                passwordEncoder.encode(dto.getPassword())
        );

        userRepository.save(user);
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_FOUND_USER));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new AuthException(ErrorCode.INVALID_CREDENTIAL);
        }

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRole().name());
        String refreshToken = tokenProvider.createRefreshToken(user.getId());

        // DB에 RefreshToken 저장 (이미 있으면 update)
        refreshTokenService.saveOrUpdate(user.getId(), refreshToken);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role("ROLE_" + user.getRole().name())
                .authProvider(user.getProvider())
                .build();
    }

    @Transactional
    public TokenDto refresh(RefreshTokenRequestDto request) {
        if (!tokenProvider.validateToken(request.getRefreshToken())) {
            throw new AuthException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        Long userId = Long.parseLong(tokenProvider.getUserIdFromToken(request.getRefreshToken()));
        String savedToken = refreshTokenService.findTokenByUserId(userId);

        // ✅ 요청으로 들어온 RefreshToken이 DB와 다르면 위조된 것
        if (savedToken == null || !savedToken.equals(request.getRefreshToken())) {
            throw new AuthException(ErrorCode.TOKEN_INVALID);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(ErrorCode.NOT_FOUND_USER));

        String newAccessToken = tokenProvider.createAccessToken(user.getId(), user.getRole().name());

        return TokenDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(request.getRefreshToken())
                .build();
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenService.deleteByUserId(userId);
    }
}
