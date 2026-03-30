package com.example.secureapihateoas.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class EventRequestDTO {

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @Size(max = 2000, message = "La description ne peut dépasser 2000 caractères")
    private String description;

    @NotNull(message = "La date de l'événement est obligatoire")
    @Future(message = "La date doit être dans le futur")
    private LocalDateTime eventDate;

    @NotBlank(message = "Le lieu est obligatoire")
    private String location;

    @NotNull(message = "Le nombre de participants max est obligatoire")
    @Min(value = 1, message = "Le nombre de participants doit être au moins 1")
    private Integer maxParticipants;

    /** IDs des catégories à associer */
    private List<Long> categoryIds;
}
