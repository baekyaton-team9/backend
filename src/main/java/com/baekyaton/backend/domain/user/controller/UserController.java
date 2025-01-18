package com.baekyaton.backend.domain.user.controller;

import com.baekyaton.backend.domain.user.dto.UserInfoResponse;
import com.baekyaton.backend.domain.user.dto.UserLikesResponse;
import com.baekyaton.backend.domain.user.service.UserService;
import com.baekyaton.backend.global.oauth2.dto.CustomOAuth2User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController implements UserApi {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> userInfo(@AuthenticationPrincipal CustomOAuth2User principal) {
        return ResponseEntity.ok(userService.getUserInfo(principal.getUserId()));
    }

    @GetMapping("/me/like")
    public ResponseEntity<UserLikesResponse> userLikes(@AuthenticationPrincipal CustomOAuth2User principal){
        return ResponseEntity.ok(userService.getUserLikes(principal.getUserId()));
    }
}
