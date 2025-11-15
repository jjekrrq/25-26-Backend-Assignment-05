package com.example.seungchang.app.domain.service;

import com.example.seungchang.app.dto.service.FoodRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private String foodName;
    private int amountOfSelling;
    private int kcal;

    @Builder(access = AccessLevel.PROTECTED)
    private Food(String foodName, int kcal, Restaurant restaurant){
        this.foodName = foodName;
        this.kcal = kcal;
        this.amountOfSelling = 0;
        this.restaurant = restaurant;
    }

    public static Food createFood(String foodName, int kcal, Restaurant restaurant){
        validate(foodName, kcal);
        return new Food(foodName, kcal, restaurant);
    }

    private static void validate(String foodName, int kcal) {
        if (foodName == null || foodName.isBlank())
            throw new IllegalArgumentException("음식 이름은 비어 있을 수 없습니다.");
        if (kcal <= 0)
            throw new IllegalArgumentException("칼로리가 0 이하일 수 없습니다.");
    }

    public void update(FoodRequestDto foodRequestDto){
        this.foodName = foodRequestDto.getFoodName();
        this.kcal = foodRequestDto.getKcal();
    }

    public void increaseSales() {
        this.amountOfSelling += 1;
    }

    public void resetSales() {
        this.amountOfSelling = 0;
    }
}
