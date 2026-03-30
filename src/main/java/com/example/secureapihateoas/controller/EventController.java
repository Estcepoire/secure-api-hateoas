package com.example.secureapihateoas.controller;

import com.example.secureapihateoas.dto.EventRequestDTO;
import com.example.secureapihateoas.dto.EventResponseDTO;
import com.example.secureapihateoas.service.EventService;
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
@RequestMapping("/api/events")
@Tag(name = "Events", description = "CRUD des événements")
@SecurityRequirement(name = "bearerAuth")
public class EventController {

    @Autowired private EventService eventService;

    @GetMapping
    @Operation(summary = "Lister tous les événements")
    public ResponseEntity<List<EventResponseDTO>> getAll() {
        return ResponseEntity.ok(eventService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un événement par son ID")
    public ResponseEntity<EventResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel événement")
    public ResponseEntity<EventResponseDTO> create(@Valid @RequestBody EventRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un événement existant")
    public ResponseEntity<EventResponseDTO> update(@PathVariable Long id,
                                                   @Valid @RequestBody EventRequestDTO dto) {
        return ResponseEntity.ok(eventService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un événement")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        eventService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
