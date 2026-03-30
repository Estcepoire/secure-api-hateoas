package com.example.secureapihateoas.controller;

import com.example.secureapihateoas.dto.ReservationRequestDTO;
import com.example.secureapihateoas.dto.ReservationResponseDTO;
import com.example.secureapihateoas.entities.ReservationStatus;
import com.example.secureapihateoas.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@Tag(name = "Reservations", description = "CRUD des réservations")
@SecurityRequirement(name = "bearerAuth")
public class ReservationController {

    @Autowired private ReservationService reservationService;

    @GetMapping
    @Operation(summary = "Lister toutes les réservations (filtres : userId, eventId, status)")
    public ResponseEntity<List<ReservationResponseDTO>> getAll(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long eventId,
            @RequestParam(required = false) ReservationStatus status) {
        return ResponseEntity.ok(reservationService.getAll(userId, eventId, status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une réservation par son ID")
    public ResponseEntity<ReservationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getById(id));
    }

    @GetMapping("/event/{eventId}")
    @Operation(summary = "Lister les réservations d'un événement")
    public ResponseEntity<List<ReservationResponseDTO>> getByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(reservationService.getByEvent(eventId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Lister les réservations d'un utilisateur")
    public ResponseEntity<List<ReservationResponseDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reservationService.getByUser(userId));
    }

    @PostMapping
    @Operation(summary = "Créer une réservation")
    public ResponseEntity<ReservationResponseDTO> create(@Valid @RequestBody ReservationRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.create(dto));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Changer le statut d'une réservation (CONFIRMED / CANCELLED / PENDING)")
    public ResponseEntity<ReservationResponseDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam ReservationStatus status) {
        return ResponseEntity.ok(reservationService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une réservation")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
