package com.example.secureapihateoas.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CategoryRequestDTO {

    @NotBlank(message = "Le nom est obligatoire")
    private String name;

    @Size(max = 500, message = "La description ne peut dépasser 500 caractères")
    private String description;
}
