package com.example.secureapihateoas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Dashboard détaillé d'un événement spécifique")
public class EventDashboardDTO extends RepresentationModel<EventDashboardDTO> {

    // ─── Infos générales ─────────────────────────────────────────────
    @Schema(description = "Identifiant de l'événement", example = "1")
    private Long eventId;

    @Schema(description = "Titre de l'événement", example = "Concert Jazz")
    private String title;

    @Schema(description = "Description de l'événement")
    private String description;

    @Schema(description = "Lieu", example = "Paris")
    private String location;

    @Schema(description = "Date de l'événement", example = "2025-06-15T20:00:00")
    private LocalDateTime eventDate;

    @Schema(description = "Catégories associées à l'événement")
    private List<String> categories;

    // ─── Capacité & remplissage ──────────────────────────────────────
    @Schema(description = "Capacité maximale", example = "200")
    private int maxParticipants;

    @Schema(description = "Places disponibles restantes", example = "50")
    private int availablePlaces;

    @Schema(description = "Taux de remplissage en %", example = "75.0")
    private double fillRate;

    @Schema(description = "L'événement a-t-il encore des places disponibles ?", example = "true")
    private boolean hasAvailablePlaces;

    // ─── Réservations ────────────────────────────────────────────────
    @Schema(description = "Nombre total de réservations pour cet événement", example = "180")
    private long totalReservations;

    @Schema(description = "Réservations confirmées", example = "150")
    private long confirmedReservations;

    @Schema(description = "Réservations annulées", example = "20")
    private long cancelledReservations;

    @Schema(description = "Réservations en attente", example = "10")
    private long pendingReservations;

    @Schema(description = "Répartition des réservations par statut")
    private Map<String, Long> reservationsByStatus;

    // ─── Avis ────────────────────────────────────────────────────────
    @Schema(description = "Nombre total d'avis", example = "85")
    private long totalReviews;

    @Schema(description = "Note moyenne (sur 5)", example = "4.3")
    private double averageRating;

    @Schema(description = "Distribution des notes (1→5 → nombre d'avis)")
    private Map<Integer, Long> ratingDistribution;

    @Schema(description = "Pourcentage d'avis positifs (note >= 4)", example = "78.8")
    private double positiveReviewRate;
}
