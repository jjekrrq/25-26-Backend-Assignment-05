package com.example.seungchang.app.service;

import com.example.seungchang.app.domain.service.Restaurant;
import com.example.seungchang.app.domain.auth.User;
import com.example.seungchang.app.dto.service.RestaurantRequestDto;
import com.example.seungchang.app.dto.service.RestaurantResponseDto;
import com.example.seungchang.app.mapper.RestaurantMapper;
import com.example.seungchang.app.repository.RestaurantRepository;
import com.example.seungchang.app.repository.UserRepository;
import com.example.seungchang.global.exception.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    @Transactional
    public RestaurantResponseDto saveRestaurant(RestaurantRequestDto dto) {
        Restaurant restaurant = restaurantMapper.toEntity(dto);
        Restaurant saved = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(saved);
    }


    @Transactional
    public List<RestaurantResponseDto> getAllRestaurants(){
        return restaurantRepository.findAll().stream()
                .map(restaurantMapper::toDto)
                .toList();
    }

    @Transactional
    public RestaurantResponseDto getOneRestaurant(Long id){
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        return restaurantMapper.toDto(restaurant);
    }

    @Transactional
    public RestaurantResponseDto updateRestaurantById(Long id, RestaurantRequestDto restaurantRequestDto){
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        restaurant.update(restaurantRequestDto.getRestaurantName());
        return restaurantMapper.toDto(restaurant);
    }

    @Transactional
    public void deleteRestaurantById(Long id){
        if (restaurantRepository.findById(id).isEmpty()){
            throw new RestaurantNotFoundException(id);
        }
        restaurantRepository.deleteById(id);
    }
}
