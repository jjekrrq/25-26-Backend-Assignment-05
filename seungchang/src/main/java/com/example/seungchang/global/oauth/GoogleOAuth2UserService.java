package com.example.seungchang.global.oauth;

import com.example.seungchang.app.domain.auth.AuthProvider;
import com.example.seungchang.app.domain.auth.User;
import com.example.seungchang.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GoogleOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        // 1) 구글에서 사용자 정보 조회
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String providerId = oAuth2User.getAttribute("sub"); // 구글 고유 ID

        if (email == null) {
            throw new OAuth2AuthenticationException("구글 계정에서 이메일을 가져올 수 없습니다.");
        }

        // 2) DB에 유저 조회 또는 생성
        AuthProvider provider = AuthProvider.GOOGLE;

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(
                        User.createSocialUser(
                                email,
                                name != null ? name : "GoogleUser",
                                provider,
                                providerId
                        )
                ));

        // 3) SecurityContext 에 넣어줄 principal 구성
        Collection<? extends GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        // 기존 구글 attributes + 우리 시스템의 userId, role 추가
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("userId", user.getId());
        attributes.put("role", user.getRole().name()); // "USER", "ADMIN"

        // key 를 "email" 로 사용 (getName() 할 때 사용)
        return new DefaultOAuth2User(authorities, attributes, "email");
    }
}
