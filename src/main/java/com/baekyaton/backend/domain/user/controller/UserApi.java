package com.baekyaton.backend.domain.user.controller;

import com.baekyaton.backend.domain.user.dto.UserInfoResponse;
import com.baekyaton.backend.domain.user.dto.UserLikesResponse;
import com.baekyaton.backend.global.oauth2.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "User", description = "사용자 API")
public interface UserApi {
    @Operation(summary = "사용자 정보 조회", description = "사용자 정보를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "사용자 정보 조회 성공"
                    )
            }
    )
    ResponseEntity<UserInfoResponse> userInfo(@AuthenticationPrincipal CustomOAuth2User principal);

    @Operation(summary = "사용자 관심 목록 조회", description = "사용자의 관심 목록을 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "사용자 관심 목록 조회 성공"
                    )
            }
    )
    ResponseEntity<UserLikesResponse> userLikes(@AuthenticationPrincipal CustomOAuth2User principal);
}
