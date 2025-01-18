package com.baekyaton.backend.domain.house.dto;

import com.baekyaton.backend.domain.house.entity.House;
import com.baekyaton.backend.domain.user.dto.UserDetail;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HouseDetail {
    private Long houseId;
    private UserDetail user;
    private String title;
    private String thumbnailUrl;
    private List<String> imageUrls;
    private String phoneNumber;
    private String address;
    private int size;
    private String description;
    private int price;
    private int likeCount;
    private List<String> tags;

    public static HouseDetail from(House house) {
        return HouseDetail.builder()
                .houseId(house.getId())
                .user(UserDetail.from(house.getUser()))
                .title(house.getTitle())
                .thumbnailUrl(house.getThumbnailUrl())
                .imageUrls(house.getImageUrls())
                .phoneNumber(house.getPhoneNumber())
                .address(house.getAddress())
                .size(house.getSize())
                .description(house.getDescription())
                .price(house.getPrice())
                .likeCount(house.getLikeCount())
                .tags(house.getTags())
                .build();
    }
}
