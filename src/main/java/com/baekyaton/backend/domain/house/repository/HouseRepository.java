package com.baekyaton.backend.domain.house.repository;

import com.baekyaton.backend.domain.house.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HouseRepository extends JpaRepository<House, Long> {
}
