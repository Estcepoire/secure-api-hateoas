package com.example.secureapihateoas.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ReviewRequestDTO {

    @NotNull(message = "L'identifiant de l'événement est obligatoire")
    private Long eventId;

    @NotNull(message = "L'identifiant de l'utilisateur est obligatoire")
    private Long userId;

    @NotNull(message = "La note est obligatoire")
    @Min(value = 1, message = "La note minimale est 1")
    @Max(value = 5, message = "La note maximale est 5")
    private Integer rating;

    @Size(max = 1000, message = "Le commentaire ne peut dépasser 1000 caractères")
    private String comment;
}
