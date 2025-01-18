package com.baekyaton.backend.domain.house.repository;

import com.baekyaton.backend.domain.house.entity.HouseLike;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseLikeRepository extends JpaRepository<HouseLike, Long> {
    boolean existsByHouseIdAndUserId(Long houseId, Long userId);

    Optional<HouseLike> findByHouseIdAndUserId(Long houseId, Long userId);

    void deleteByHouseIdAndUserId(Long houseId, Long userId);

    List<HouseLike> findAllByUserId(Long userId);
}
