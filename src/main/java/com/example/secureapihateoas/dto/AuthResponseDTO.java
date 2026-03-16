package com.example.secureapihateoas.dto;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor
public class AuthResponseDTO {
    private String token;
    private String email;
    private String role;
    private String message;
}
