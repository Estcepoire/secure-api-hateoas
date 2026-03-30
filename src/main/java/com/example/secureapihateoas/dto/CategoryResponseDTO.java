package com.example.secureapihateoas.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class CategoryResponseDTO extends RepresentationModel<CategoryResponseDTO> {

    private Long id;
    private String name;
    private String description;
    private int eventCount;
}
