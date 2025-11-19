package com.example.seungchang.app.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @Email
    @NotBlank
    private static String email;

    @NotBlank
    private static String password;
}
