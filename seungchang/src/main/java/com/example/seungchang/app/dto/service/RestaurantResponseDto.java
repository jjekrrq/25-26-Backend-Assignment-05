package com.example.seungchang.app.dto.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class RestaurantResponseDto {
    private Long id;
    private String restaurantName;
    private String bossName;
    private int countOfFood;
    private List<FoodResponseDto> foodList;
}
