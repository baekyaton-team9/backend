package com.baekyaton.backend.domain.house.repository;

import com.baekyaton.backend.domain.house.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findAllByUserId(Long userId);
}
