package com.example.secureapihateoas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    private String name;
}
