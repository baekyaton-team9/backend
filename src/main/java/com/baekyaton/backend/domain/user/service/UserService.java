package com.baekyaton.backend.domain.user.service;


import com.baekyaton.backend.domain.house.dto.HouseDetail;
import com.baekyaton.backend.domain.house.entity.House;
import com.baekyaton.backend.domain.house.entity.HouseLike;
import com.baekyaton.backend.domain.house.repository.HouseLikeRepository;
import com.baekyaton.backend.domain.house.repository.HouseRepository;
import com.baekyaton.backend.domain.house.repository.HouseTagRepository;
import com.baekyaton.backend.domain.user.dto.UserDetail;
import com.baekyaton.backend.domain.user.dto.UserInfoResponse;
import com.baekyaton.backend.domain.user.dto.UserLikesResponse;
import com.baekyaton.backend.domain.user.entity.User;
import com.baekyaton.backend.domain.user.exception.UserErrorCode;
import com.baekyaton.backend.domain.user.repository.UserRepository;
import com.baekyaton.backend.global.exception.ApiException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final HouseLikeRepository houseLikeRepository;

    @Transactional
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        UserDetail userDetail = UserDetail.from(user);

        List<House> houses = houseRepository.findAllByUserId(userId);

        List<HouseDetail> houseDetails = houses.stream().map(HouseDetail::from).toList();

        return UserInfoResponse.from(userDetail, houseDetails);
    }

    @Transactional
    public UserLikesResponse getUserLikes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));

        List<HouseLike> houseLikes = houseLikeRepository.findAllByUserId(userId);

        List<HouseDetail> houseDetails = houseLikes.stream()
                .map((houseLike) -> HouseDetail.from(houseLike.getHouse()))
                .toList();

        return UserLikesResponse.from(houseDetails);
    }

}
