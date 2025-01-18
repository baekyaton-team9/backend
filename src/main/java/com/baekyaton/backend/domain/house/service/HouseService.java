package com.baekyaton.backend.domain.house.service;

import com.baekyaton.backend.domain.house.dto.HouseCreateRequest;
import com.baekyaton.backend.domain.house.dto.HouseDetail;
import com.baekyaton.backend.domain.house.entity.House;
import com.baekyaton.backend.domain.house.entity.HouseLike;
import com.baekyaton.backend.domain.house.exception.HouseErrorCode;
import com.baekyaton.backend.domain.house.repository.HouseLikeRepository;
import com.baekyaton.backend.domain.house.repository.HouseRepository;
import com.baekyaton.backend.domain.user.entity.User;
import com.baekyaton.backend.domain.user.exception.UserErrorCode;
import com.baekyaton.backend.domain.user.repository.UserRepository;
import com.baekyaton.backend.global.exception.ApiException;
import com.baekyaton.backend.global.exception.GlobalErrorCode;
import com.baekyaton.backend.global.external.s3.S3Service;
import java.util.ArrayList;
import java.util.Comparator;
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
    private final UserRepository userRepository;
    private final HouseLikeRepository houseLikeRepository;

    public List<HouseDetail> getAllHouses() {
        return houseRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(House::getCreatedAt).reversed())
                .map(HouseDetail::from)
                .toList();
    }

    @Transactional
    public void createHouse(Long userId, MultipartFile thumbnail, List<MultipartFile> images, HouseCreateRequest request) {
        if (thumbnail == null || thumbnail.isEmpty()) {
            throw new ApiException(HouseErrorCode.IMAGE_NOT_FOUND);
        }

        List<String> imageUrls = new ArrayList<>();
        String thumbnailUrl = uploadHouseImage(thumbnail, "thumbnails");

        if (!images.isEmpty()) {
            images.forEach(image -> {
                String imageUrl = uploadHouseImage(image, "images");
                imageUrls.add(imageUrl);
            });
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        House house = House
                .builder()
                .title(request.getTitle())
                .user(user)
                .phoneNumber(request.getPhoneNumber())
                .thumbnailUrl(thumbnailUrl)
                .imageUrls(imageUrls)
                .address(request.getAddress())
                .size(request.getSize())
                .description(request.getDescription())
                .price(request.getPrice())
                .likeCount(0)
                .tags(request.getTags())
                .build();

        houseRepository.save(house);
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

        if (houseLikeRepository.existsByHouseIdAndUserId(houseId, userId)) {
            houseLikeRepository.deleteByHouseIdAndUserId(houseId, userId);
            house.postDislike();
            return;
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        HouseLike houseLike = HouseLike.builder()
                .house(house)
                .user(user)
                .build();

        houseLikeRepository.save(houseLike);

        house.postLike();
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
