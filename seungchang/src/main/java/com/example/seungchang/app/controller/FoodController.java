package com.example.seungchang.app.controller;

import com.example.seungchang.app.dto.service.FoodRequestDto;
import com.example.seungchang.app.dto.service.FoodResponseDto;
import com.example.seungchang.app.service.FoodService;
import com.example.seungchang.global.code.SuccessCode;
import com.example.seungchang.global.responseTemplate.ApiResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/food")
public class FoodController {
    private final FoodService foodService;

    @PostMapping("/create/{id}")
    public ResponseEntity<ApiResponseTemplate<FoodResponseDto>> createFood(@PathVariable Long id, @RequestBody @Valid FoodRequestDto foodRequestDto){
        FoodResponseDto foodResponseDto = foodService.createFood(id, foodRequestDto);
        return ApiResponseTemplate.success(SuccessCode.FOOD_CREATED, foodResponseDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponseTemplate<FoodResponseDto>> updateFood(@PathVariable Long id, @RequestBody @Valid FoodRequestDto foodRequestDto){
        FoodResponseDto foodResponseDto = foodService.updateFood(id, foodRequestDto);
        return ApiResponseTemplate.success(SuccessCode.FOOD_UPDATED, foodResponseDto);
    }

    @PostMapping("/sell/{id}")
    public ResponseEntity<ApiResponseTemplate<FoodResponseDto>> sellFood(@PathVariable Long id) {
        FoodResponseDto foodResponseDto = foodService.sellFood(id);
        return ApiResponseTemplate.success(SuccessCode.FOOD_SOLD, foodResponseDto);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponseTemplate<Void>> deleteFood(@PathVariable Long id){
        foodService.deleteFood(id);
        return ApiResponseTemplate.success(SuccessCode.FOOD_DELETED, null);
    }
}
