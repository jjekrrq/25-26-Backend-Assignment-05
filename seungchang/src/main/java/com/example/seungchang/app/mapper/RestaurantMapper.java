package com.example.seungchang.app.mapper;

import com.example.seungchang.app.domain.service.Food;
import com.example.seungchang.app.domain.service.Restaurant;
import com.example.seungchang.app.domain.auth.User;
import com.example.seungchang.app.dto.service.FoodResponseDto;
import com.example.seungchang.app.dto.service.RestaurantRequestDto;
import com.example.seungchang.app.dto.service.RestaurantResponseDto;
import com.example.seungchang.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestaurantMapper {
    private final UserRepository userRepository;
    public Restaurant toEntity(RestaurantRequestDto restaurantRequestDto){
        //  현재 인증 정보에서 userId(subject) 꺼내기
        String userIdStr = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName(); // TokenProvider에서 subject로 userId를 넣었기 때문

        Long userId = Long.parseLong(userIdStr);

        //  유저 조회 → 이름 가져오기
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("로그인한 사용자를 찾을 수 없습니다."));

        String bossName = user.getName(); // 사장 이름 = 로그인한 유저 이름
        Restaurant restaurant = Restaurant.create(restaurantRequestDto.getRestaurantName(), bossName);
        return restaurant;
    }

    public RestaurantResponseDto toDto(Restaurant restaurant) {
        List<FoodResponseDto> foodDtos = toFoodDtoList(restaurant);

        return RestaurantResponseDto.builder()
                .id(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
                .bossName(restaurant.getBossName())
                .countOfFood(foodDtos.size())
                .foodList(foodDtos)
                .build();
    }

    private List<FoodResponseDto> toFoodDtoList(Restaurant restaurant) {
        return restaurant.getFoodList().stream()
                .map(food -> toFoodDto(food, restaurant))
                .toList();
    }

    private FoodResponseDto toFoodDto(Food food, Restaurant restaurant) {
        return FoodResponseDto.builder()
                .id(food.getId())
                .foodName(food.getFoodName())
                .kcal(food.getKcal())
                .amountOfSelling(food.getAmountOfSelling())
                .restaurantId(restaurant.getId())
                .restaurantName(restaurant.getRestaurantName())
                .build();
    }
}
