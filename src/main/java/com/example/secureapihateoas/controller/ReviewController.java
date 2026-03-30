package com.example.secureapihateoas.controller;

import com.example.secureapihateoas.dto.ReviewRequestDTO;
import com.example.secureapihateoas.dto.ReviewResponseDTO;
import com.example.secureapihateoas.service.ReviewService;
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
@RequestMapping("/api/reviews")
@Tag(name = "Reviews", description = "CRUD des avis")
@SecurityRequirement(name = "bearerAuth")
public class ReviewController {

    @Autowired private ReviewService reviewService;

    @GetMapping
    @Operation(summary = "Lister tous les avis")
    public ResponseEntity<List<ReviewResponseDTO>> getAll() {
        return ResponseEntity.ok(reviewService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un avis par son ID")
    public ResponseEntity<ReviewResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getById(id));
    }

    @GetMapping("/event/{eventId}")
    @Operation(summary = "Lister les avis d'un événement (du plus récent au plus ancien)")
    public ResponseEntity<List<ReviewResponseDTO>> getByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(reviewService.getByEvent(eventId));
    }

    @PostMapping
    @Operation(summary = "Poster un avis (note 1-5)")
    public ResponseEntity<ReviewResponseDTO> create(@Valid @RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un avis (note & commentaire)")
    public ResponseEntity<ReviewResponseDTO> update(@PathVariable Long id,
                                                    @Valid @RequestBody ReviewRequestDTO dto) {
        return ResponseEntity.ok(reviewService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un avis")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
