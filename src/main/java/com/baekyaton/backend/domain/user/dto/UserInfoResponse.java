package com.baekyaton.backend.domain.user.dto;

import com.baekyaton.backend.domain.house.dto.HouseDetail;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserInfoResponse {
    private UserDetail user;
    private List<HouseDetail> houses;

    public static UserInfoResponse from(UserDetail user, List<HouseDetail> houses) {
        return UserInfoResponse.builder()
                .user(user)
                .houses(houses)
                .build();
    }
}
