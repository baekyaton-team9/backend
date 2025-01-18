package com.baekyaton.backend.domain.user.dto;

import com.baekyaton.backend.domain.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetail {
    private Long userId;
    private String name;
    private String profileImageUrl;

    public static UserDetail from(User user) {
        return UserDetail.builder()
                .userId(user.getId())
                .name(user.getName())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }
}
