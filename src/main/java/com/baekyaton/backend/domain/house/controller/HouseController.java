package com.baekyaton.backend.domain.house.controller;

import com.baekyaton.backend.domain.house.dto.HouseCreateRequest;
import com.baekyaton.backend.domain.house.dto.HouseDetail;
import com.baekyaton.backend.domain.house.service.HouseService;
import com.baekyaton.backend.global.oauth2.dto.CustomOAuth2User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/houses")
@RequiredArgsConstructor
public class HouseController {
    private final HouseService houseService;

    @GetMapping
    public ResponseEntity<List<HouseDetail>> getAllHouses() {
        return ResponseEntity.ok(houseService.getAllHouses());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> createHouse(
            @RequestPart MultipartFile thumbnail,
            @RequestPart List<MultipartFile> images,
            @RequestPart HouseCreateRequest houseCreateRequest) {
        houseService.createHouse(thumbnail, images, houseCreateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{houseId}")
    public ResponseEntity<HouseDetail> getHouse(@PathVariable Long houseId) {
        return ResponseEntity.ok(houseService.getHouse(houseId));
    }

    @PostMapping("/likes/{houseId}")
    public ResponseEntity<Void> postLikeHouse(
            @AuthenticationPrincipal CustomOAuth2User principal,
            @PathVariable Long houseId
    ) {
        houseService.postHouseLike(houseId, principal.getUserId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{houseId}")
    public ResponseEntity<Void> deleteHouse(@PathVariable Long houseId) {
        houseService.deleteHouse(houseId);
        return ResponseEntity.ok().build();
    }
}
