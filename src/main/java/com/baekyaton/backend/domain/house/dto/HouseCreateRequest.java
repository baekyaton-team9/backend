package com.baekyaton.backend.domain.house.dto;

import java.util.List;
import lombok.Data;

@Data
public class HouseCreateRequest {
    private String title;
    private String phoneNumber;
    private String address;
    private int size;
    private String description;
    private int price;
    private List<String> tags;
}
