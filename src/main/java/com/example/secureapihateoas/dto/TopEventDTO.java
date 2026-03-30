package com.example.secureapihateoas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Statistiques d'un événement populaire")
public class TopEventDTO {

    @Schema(description = "Identifiant de l'événement", example = "1")
    private Long eventId;

    @Schema(description = "Titre de l'événement", example = "Concert Jazz")
    private String title;

    @Schema(description = "Lieu de l'événement", example = "Paris")
    private String location;

    @Schema(description = "Nombre de places maximum", example = "200")
    private int maxParticipants;

    @Schema(description = "Nombre de réservations confirmées", example = "150")
    private long confirmedReservations;

    @Schema(description = "Taux de remplissage en pourcentage", example = "75.0")
    private double fillRate;

    @Schema(description = "Note moyenne des avis (0 si aucun avis)", example = "4.3")
    private double averageRating;

    @Schema(description = "Nombre total d'avis", example = "42")
    private long reviewCount;
}
