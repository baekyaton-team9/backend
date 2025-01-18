package com.baekyaton.backend.domain.house.dto;

import com.baekyaton.backend.domain.house.entity.House;
import com.baekyaton.backend.domain.user.dto.UserDetail;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HouseDetail {
    private Long houseId;
    private UserDetail user;
    private String title;
    private String imageUrl;
    private String phoneNumber;
    private String address;
    private int minSize;
    private int maxSize;
    private String description;
    private int price;

    public static HouseDetail from(House house) {
        return HouseDetail.builder()
                .houseId(house.getId())
                .user(UserDetail.from(house.getUser()))
                .title(house.getTitle())
                .imageUrl(house.getImageUrl())
                .phoneNumber(house.getPhoneNumber())
                .address(house.getAddress())
                .minSize(house.getMinSize())
                .maxSize(house.getMaxSize())
                .description(house.getDescription())
                .price(house.getPrice())
                .build();

    }
}
