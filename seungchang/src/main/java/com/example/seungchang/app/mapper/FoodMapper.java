package com.example.seungchang.app.mapper;

import com.example.seungchang.app.domain.service.Food;
import com.example.seungchang.app.domain.service.Restaurant;
import com.example.seungchang.app.dto.service.FoodRequestDto;
import com.example.seungchang.app.dto.service.FoodResponseDto;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {
    public Food toEntity(FoodRequestDto foodRequestDto, Restaurant restaurant){
        return Food.createFood(foodRequestDto.getFoodName(), foodRequestDto.getKcal(), restaurant);
    }

    public FoodResponseDto toDto(Food food){
        return FoodResponseDto.builder()
                .id(food.getId())
                .foodName(food.getFoodName())
                .kcal(food.getKcal())
                .amountOfSelling(food.getAmountOfSelling())
                .restaurantId(food.getRestaurant().getId())
                .restaurantName(food.getRestaurant().getRestaurantName())
                .build();
    }
}
