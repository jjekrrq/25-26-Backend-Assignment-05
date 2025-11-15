package com.example.seungchang.app.controller;

import com.example.seungchang.app.dto.service.RestaurantRequestDto;
import com.example.seungchang.app.dto.service.RestaurantResponseDto;
import com.example.seungchang.app.service.RestaurantService;
import com.example.seungchang.global.code.SuccessCode;
import com.example.seungchang.global.responseTemplate.ApiResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    // 식당 생성
    @PostMapping("/create")
    public ResponseEntity<ApiResponseTemplate<RestaurantResponseDto>> createRestaurant(@RequestBody @Valid RestaurantRequestDto restaurantRequestDto){
        RestaurantResponseDto restaurantResponseDto = restaurantService.saveRestaurant(restaurantRequestDto);
        return ApiResponseTemplate.success(SuccessCode.RESTAURANT_CREATED, restaurantResponseDto);
    }
    // 전체 식당 조회
    @GetMapping
    public ResponseEntity<ApiResponseTemplate<List<RestaurantResponseDto>>> getAllRestaurant(){
        List<RestaurantResponseDto> restaurants = restaurantService.getAllRestaurants();
        return ApiResponseTemplate.success(SuccessCode.RESTAURANT_FOUND, restaurants);
    }
    // id로 식당 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseTemplate<RestaurantResponseDto>> getRestaurantById(@PathVariable Long id){
        RestaurantResponseDto restaurantResponseDto = restaurantService.getOneRestaurant(id);
        return ApiResponseTemplate.success(SuccessCode.RESTAURANT_FOUND, restaurantResponseDto);
    }
    // 식당 업데이트
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponseTemplate<RestaurantResponseDto>> updateRestaurant(@PathVariable Long id, @RequestBody @Valid RestaurantRequestDto restaurantRequestDto){
        RestaurantResponseDto restaurantResponseDto = restaurantService.updateRestaurantById(id, restaurantRequestDto);
        return ApiResponseTemplate.success(SuccessCode.RESTAURANT_UPDATED, restaurantResponseDto);
    }
    // 식당 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseTemplate<Void>> deleteById(@PathVariable Long id){
        restaurantService.deleteRestaurantById(id);
        return ApiResponseTemplate.success(SuccessCode.RESTAURANT_DELETED, null);
    }
}
