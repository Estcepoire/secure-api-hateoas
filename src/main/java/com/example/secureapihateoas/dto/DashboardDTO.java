package com.example.secureapihateoas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Map;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Schema(description = "Dashboard global de la plateforme")
public class DashboardDTO extends RepresentationModel<DashboardDTO> {

    // ─── Utilisateurs ───────────────────────────────────────────────
    @Schema(description = "Nombre total d'utilisateurs inscrits", example = "320")
    private long totalUsers;

    // ─── Événements ─────────────────────────────────────────────────
    @Schema(description = "Nombre total d'événements", example = "45")
    private long totalEvents;

    @Schema(description = "Nombre d'événements avec des places disponibles", example = "30")
    private long eventsWithAvailablePlaces;

    @Schema(description = "Taux de remplissage moyen de tous les événements (en %)", example = "62.5")
    private double averageFillRate;

    // ─── Réservations ────────────────────────────────────────────────
    @Schema(description = "Nombre total de réservations", example = "850")
    private long totalReservations;

    @Schema(description = "Nombre de réservations confirmées", example = "700")
    private long confirmedReservations;

    @Schema(description = "Nombre de réservations annulées", example = "100")
    private long cancelledReservations;

    @Schema(description = "Nombre de réservations en attente", example = "50")
    private long pendingReservations;

    @Schema(description = "Répartition des réservations par statut (CONFIRMED, CANCELLED, PENDING)")
    private Map<String, Long> reservationsByStatus;

    // ─── Avis ────────────────────────────────────────────────────────
    @Schema(description = "Nombre total d'avis", example = "430")
    private long totalReviews;

    @Schema(description = "Note globale moyenne (sur 5)", example = "4.1")
    private double globalAverageRating;

    // ─── Catégories ──────────────────────────────────────────────────
    @Schema(description = "Nombre total de catégories", example = "8")
    private long totalCategories;

    @Schema(description = "Nombre d'événements par catégorie")
    private Map<String, Long> eventsByCategory;

    // ─── Top événements ──────────────────────────────────────────────
    @Schema(description = "Top 5 événements par taux de remplissage")
    private List<TopEventDTO> topEventsByFillRate;

    @Schema(description = "Top 5 événements par note moyenne")
    private List<TopEventDTO> topEventsByRating;
}
