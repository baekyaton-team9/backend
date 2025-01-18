package com.baekyaton.backend.domain.house.service;

import com.baekyaton.backend.domain.house.dto.HouseCreateRequest;
import com.baekyaton.backend.domain.house.dto.HouseDetail;
import com.baekyaton.backend.domain.house.entity.House;
import com.baekyaton.backend.domain.house.entity.HouseImage;
import com.baekyaton.backend.domain.house.entity.HouseLike;
import com.baekyaton.backend.domain.house.entity.HouseTagInfo;
import com.baekyaton.backend.domain.house.enums.HouseTag;
import com.baekyaton.backend.domain.house.exception.HouseErrorCode;
import com.baekyaton.backend.domain.house.repository.HouseImageRepository;
import com.baekyaton.backend.domain.house.repository.HouseLikeRepository;
import com.baekyaton.backend.domain.house.repository.HouseRepository;
import com.baekyaton.backend.domain.house.repository.HouseTagRepository;
import com.baekyaton.backend.domain.user.entity.User;
import com.baekyaton.backend.domain.user.exception.UserErrorCode;
import com.baekyaton.backend.domain.user.repository.UserRepository;
import com.baekyaton.backend.global.exception.ApiException;
import com.baekyaton.backend.global.exception.GlobalErrorCode;
import com.baekyaton.backend.global.external.s3.S3Service;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HouseService {
    private final HouseRepository houseRepository;
    private final S3Service s3Service;
    private final HouseTagRepository houseTagRepository;
    private final UserRepository userRepository;
    private final HouseLikeRepository houseLikeRepository;
    private final HouseImageRepository houseImageRepository;

    public List<HouseDetail> getAllHouses() {
        return houseRepository.findAll()
                .stream()
                .map(HouseDetail::from)
                .toList();
    }

    @Transactional
    public void createHouse(MultipartFile thumbnail, List<MultipartFile> images, HouseCreateRequest request) {
        if (thumbnail == null || thumbnail.isEmpty()) {
            throw new ApiException(HouseErrorCode.IMAGE_NOT_FOUND);
        }

        String thumbnailUrl = uploadHouseImage(thumbnail, "thumbnails");

        House house = House
                .builder()
                .title(request.getTitle())
                .phoneNumber(request.getPhoneNumber())
                .thumbnailUrl(thumbnailUrl)
                .address(request.getAddress())
                .size(request.getSize())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        House savedHouse = houseRepository.save(house);

        request.getTags()
                .stream()
                .map(HouseTag::valueOf)
                .forEach(houseTag -> {
                    HouseTagInfo houseTagInfo = HouseTagInfo.builder()
                            .house(savedHouse)
                            .tag(houseTag)
                            .build();

                    houseTagRepository.save(houseTagInfo);
                });

        if (!images.isEmpty()) {
            images.forEach(image -> {
                String imageUrl = uploadHouseImage(image, "images");
                HouseImage houseImage = HouseImage.builder()
                        .imageUrl(imageUrl)
                        .house(savedHouse)
                        .build();

                houseImageRepository.save(houseImage);
            });
        }
    }

    private String uploadHouseImage(MultipartFile image, String path) {
        try {
            String randomName = UUID.randomUUID().toString();
            return s3Service.uploadImageFile(image, "baekyaton/houses/" + path + "/" + randomName);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(GlobalErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public HouseDetail getHouse(Long houseId) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new ApiException(HouseErrorCode.HOUSE_NOT_FOUND));

        return HouseDetail.from(house);
    }

    @Transactional
    public void postHouseLike(Long houseId, Long userId) {
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new ApiException(HouseErrorCode.HOUSE_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        HouseLike houseLike = HouseLike.builder()
                .house(house)
                .user(user)
                .build();

        houseLikeRepository.save(houseLike);
    }

    @Transactional
    public void deleteHouse(Long houseId) {
        try {
            houseRepository.deleteById(houseId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ApiException(HouseErrorCode.HOUSE_NOT_FOUND);
        }
    }
}
