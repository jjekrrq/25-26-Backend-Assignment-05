package com.example.seungchang.app.service;

import com.example.seungchang.app.domain.auth.RefreshToken;
import com.example.seungchang.app.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveOrUpdate(Long userId, String token) {
        refreshTokenRepository.findByUserId(userId)
                .ifPresentOrElse(
                        refreshToken -> refreshToken.updateToken(token),
                        () -> refreshTokenRepository.save(
                                RefreshToken.builder()
                                        .userId(userId)
                                        .token(token)
                                        .build()
                        )
                );
    }

    public String findTokenByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId)
                .map(RefreshToken::getToken)
                .orElse(null);
    }

    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
