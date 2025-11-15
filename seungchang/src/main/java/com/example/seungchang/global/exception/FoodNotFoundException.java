package com.example.seungchang.global.exception;

public class FoodNotFoundException extends RuntimeException{
    public FoodNotFoundException(Long id){
        super("Food with id " + id + " not found.");
    }
}
