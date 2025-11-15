package com.example.seungchang.app.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FoodRequestDto {
    @NotBlank(message = "음식이름은 필수입니다")
    private String foodName;

    @NotNull(message = "칼로리 입력은 필수입니다")
    private Integer kcal;
}
