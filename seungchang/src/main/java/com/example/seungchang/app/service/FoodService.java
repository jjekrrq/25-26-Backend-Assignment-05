package com.example.seungchang.app.service;

import com.example.seungchang.app.domain.service.Food;
import com.example.seungchang.app.domain.service.Restaurant;
import com.example.seungchang.app.dto.service.FoodRequestDto;
import com.example.seungchang.app.dto.service.FoodResponseDto;
import com.example.seungchang.app.mapper.FoodMapper;
import com.example.seungchang.app.repository.FoodRepository;
import com.example.seungchang.app.repository.RestaurantRepository;
import com.example.seungchang.global.exception.FoodNotFoundException;
import com.example.seungchang.global.exception.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public FoodResponseDto createFood(Long id, FoodRequestDto foodRequestDto){
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));

        Food food = foodMapper.toEntity(foodRequestDto, restaurant);
        Food saved = foodRepository.save(food);
        return foodMapper.toDto(saved);
    }

    @Transactional
    public FoodResponseDto updateFood(Long id, FoodRequestDto foodRequestDto){
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException(id));
        food.update(foodRequestDto);
        return foodMapper.toDto(food);
    }

    @Transactional
    public FoodResponseDto sellFood(Long id) {
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new FoodNotFoundException(id));

        food.increaseSales(); // 엔티티 내부에서 +1
        return foodMapper.toDto(food);
    }


    @Transactional
    public void  deleteFood(Long id){
        if(foodRepository.findById(id).isEmpty()){
            throw new FoodNotFoundException(id);
        }
        foodRepository.deleteById(id);
    }
}
