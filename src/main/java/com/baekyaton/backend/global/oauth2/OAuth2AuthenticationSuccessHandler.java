package com.baekyaton.backend.global.oauth2;

import com.baekyaton.backend.domain.user.entity.User;
import com.baekyaton.backend.domain.user.repository.UserRepository;
import com.baekyaton.backend.global.jwt.TokenProvider;
import com.baekyaton.backend.global.oauth2.dto.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;

    @Value("${login.redirect.uri}")
    private String loginRedirectUri;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String kakaoUserId = oAuth2User.getAttributes().get("id").toString();
        System.out.println("profileImageUrl: " + oAuth2User.getProfileImageUrl());
        User user = userRepository.findByKakaoId(kakaoUserId)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .kakaoId(kakaoUserId)
                            .name(oAuth2User.getName())
                            .profileImageUrl(oAuth2User.getProfileImageUrl())
                            .build();
                    return userRepository.save(newUser);
                });

        String accessToken = tokenProvider.generateToken(user, Duration.ofDays(30));
        getRedirectStrategy().sendRedirect(request, response, getRedirectUrl(loginRedirectUri, accessToken));
    }

    private String getRedirectUrl(String targetUrl, String token) {
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .build().toUriString();
    }
}