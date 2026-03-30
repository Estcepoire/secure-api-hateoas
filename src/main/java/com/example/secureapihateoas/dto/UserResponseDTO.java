package com.example.secureapihateoas.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class UserResponseDTO extends RepresentationModel<UserResponseDTO> {

    private Long id;
    private String name;
    private String email;
    private String role;
    private int reservationCount;
    private int reviewCount;
}
