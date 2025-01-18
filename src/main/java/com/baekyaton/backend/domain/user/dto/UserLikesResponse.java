package com.baekyaton.backend.domain.user.dto;

import com.baekyaton.backend.domain.house.dto.HouseDetail;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserLikesResponse {
    private List<HouseDetail> houses;

    public static UserLikesResponse from (List<HouseDetail> houses) {
        return UserLikesResponse.builder()
                .houses(houses)
                .build();
    }
}
