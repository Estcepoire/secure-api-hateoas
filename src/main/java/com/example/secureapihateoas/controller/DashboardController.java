package com.example.secureapihateoas.controller;

import com.example.secureapihateoas.dto.DashboardDTO;
import com.example.secureapihateoas.dto.EventDashboardDTO;
import com.example.secureapihateoas.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@Tag(
    name = "Dashboard",
    description = "Endpoints d'agrégation et de statistiques globales / par événement"
)
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // =========================================================================
    // GET /api/dashboard  →  Vue globale de la plateforme
    // =========================================================================

    @GetMapping
    @Operation(
        summary     = "Dashboard global de la plateforme",
        description = """
                Retourne un agrégat complet de la plateforme :
                - Nombre d'utilisateurs, d'événements, de réservations, d'avis et de catégories
                - Taux de remplissage moyen des événements
                - Répartition des réservations par statut (CONFIRMED / CANCELLED / PENDING)
                - Nombre d'événements par catégorie
                - Note globale moyenne
                - Top 5 événements par taux de remplissage et par note moyenne
                
                **Nécessite un token JWT valide.**
                """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Dashboard global récupéré avec succès",
            content      = @Content(
                mediaType = "application/json",
                schema    = @Schema(implementation = DashboardDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description  = "Authentification requise — token JWT manquant ou invalide",
            content      = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description  = "Erreur interne du serveur",
            content      = @Content
        )
    })
    public ResponseEntity<DashboardDTO> getGlobalDashboard() {
        return ResponseEntity.ok(dashboardService.getGlobalDashboard());
    }

    // =========================================================================
    // GET /api/dashboard/events/{id}  →  Vue détaillée d'un événement
    // =========================================================================

    @GetMapping("/events/{id}")
    @Operation(
        summary     = "Dashboard détaillé d'un événement",
        description = """
                Retourne les statistiques complètes pour un événement spécifique :
                - Informations générales (titre, lieu, date, catégories)
                - Capacité, places disponibles et taux de remplissage
                - Répartition des réservations par statut (CONFIRMED / CANCELLED / PENDING)
                - Note moyenne, distribution des notes (1 → 5)
                - Taux d'avis positifs (note ≥ 4)
                
                **Nécessite un token JWT valide.**
                """
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description  = "Dashboard de l'événement récupéré avec succès",
            content      = @Content(
                mediaType = "application/json",
                schema    = @Schema(implementation = EventDashboardDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description  = "Authentification requise — token JWT manquant ou invalide",
            content      = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description  = "Événement introuvable pour l'identifiant fourni",
            content      = @Content
        ),
        @ApiResponse(
            responseCode = "500",
            description  = "Erreur interne du serveur",
            content      = @Content
        )
    })
    public ResponseEntity<EventDashboardDTO> getEventDashboard(
            @Parameter(description = "Identifiant unique de l'événement", example = "1", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(dashboardService.getEventDashboard(id));
    }
}
