package com.baekyaton.backend.domain.house.controller;

import com.baekyaton.backend.domain.house.dto.HouseCreateRequest;
import com.baekyaton.backend.domain.house.dto.HouseDetail;
import com.baekyaton.backend.global.exception.ErrorResponse;
import com.baekyaton.backend.global.oauth2.dto.CustomOAuth2User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "House", description = "빈 집 API")
public interface HouseApi {
    @Operation(summary = "전체 빈 집 조회", description = "모든 빈 집 정보를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "모든 빈 집 목록 조회 성공"
                    )
            }
    )
    ResponseEntity<List<HouseDetail>> getAllHouses();

    @Operation(summary = "빈 집 등록", description = "빈 집을 등록하는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "빈 집 등록 성공"
                    )
            }
    )
    @RequestBody(content = {
            @Content(
                    encoding = @Encoding(name = "request", contentType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    ResponseEntity<Void> createHouse(
            @AuthenticationPrincipal CustomOAuth2User principal,
            @RequestPart MultipartFile thumbnail,
            @RequestPart List<MultipartFile> images,
            @RequestPart HouseCreateRequest request);

    @Operation(summary = "빈 집 상세 조회", description = "ID로 빈 집 정보를 조회하는 GET API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "빈 집 조회 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "빈 집 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<HouseDetail> getHouse(@PathVariable Long houseId);

    @Operation(summary = "빈 집에 관심 남기기", description = "ID로 빈 집에 관심을 남기는 POST API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "이미 관심을 남겼다면 해제, 아니라면 관심 남기기 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "빈 집 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> postLikeHouse(
            @AuthenticationPrincipal CustomOAuth2User principal,
            @PathVariable Long houseId
    );

    @Operation(summary = "빈 집 삭제", description = "ID로 빈 집을 삭제하는 DELETE API")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "빈 집 삭제 성공"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "빈 집 정보를 찾을 수 없습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
                    )
            }
    )
    ResponseEntity<Void> deleteHouse(@PathVariable Long houseId);
}
