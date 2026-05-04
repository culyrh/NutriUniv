package com.example.nutriuniv.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RegisterRequest {
    private String provider;
    private String oauthId;
    private String name;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(
            message = "올바른 이메일 형식을 입력해 주세요.",
            regexp = "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
    )
    private String email;

    private String gender;
    private LocalDate birthDate;
}