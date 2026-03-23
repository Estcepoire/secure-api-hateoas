package com.example.secureapihateoas.dto;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Data @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuthResponseDTO extends RepresentationModel<AuthResponseDTO> {
    private String token;
    private String email;
    private String name;
    private String role;
    private String message;
}
