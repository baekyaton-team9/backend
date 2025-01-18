package com.baekyaton.backend.domain.house.repository;

import com.baekyaton.backend.domain.house.entity.HouseTagInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseTagRepository extends JpaRepository<HouseTagInfo, Long> {
}
