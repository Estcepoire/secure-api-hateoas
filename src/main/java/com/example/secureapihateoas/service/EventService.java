package com.example.secureapihateoas.service;

import com.example.secureapihateoas.controller.EventController;
import com.example.secureapihateoas.controller.ReservationController;
import com.example.secureapihateoas.controller.ReviewController;
import com.example.secureapihateoas.dto.EventRequestDTO;
import com.example.secureapihateoas.dto.EventResponseDTO;
import com.example.secureapihateoas.dto.ReservationRequestDTO;
import com.example.secureapihateoas.entities.Category;
import com.example.secureapihateoas.entities.Event;
import com.example.secureapihateoas.repository.CategoryRepository;
import com.example.secureapihateoas.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class EventService {

    @Autowired private EventRepository eventRepository;
    @Autowired private CategoryRepository categoryRepository;

    // ─── GET ALL (avec filtrage multi-critères) ───────────────────────────────
    public List<EventResponseDTO> getAll(Long categoryId, String location) {
        return eventRepository.searchEvents(categoryId, location).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ─── GET BY ID ────────────────────────────────────────────────────────────
    public EventResponseDTO getById(Long id) {
        return toDTO(findOrThrow(id));
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────
    public EventResponseDTO create(EventRequestDTO dto) {
        List<Category> categories = resolveCategories(dto.getCategoryIds());
        Event event = Event.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .location(dto.getLocation())
                .maxParticipants(dto.getMaxParticipants())
                .categories(categories)
                .build();
        return toDTO(eventRepository.save(event));
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────
    public EventResponseDTO update(Long id, EventRequestDTO dto) {
        Event event = findOrThrow(id);
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setEventDate(dto.getEventDate());
        event.setLocation(dto.getLocation());
        event.setMaxParticipants(dto.getMaxParticipants());
        event.setCategories(resolveCategories(dto.getCategoryIds()));
        return toDTO(eventRepository.save(event));
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────
    public void delete(Long id) {
        findOrThrow(id);
        eventRepository.deleteById(id);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────
    private Event findOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Événement introuvable : " + id));
    }

    private List<Category> resolveCategories(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        return ids.stream()
                .map(cId -> categoryRepository.findById(cId)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Catégorie introuvable : " + cId)))
                .collect(Collectors.toList());
    }

    public EventResponseDTO toDTO(Event e) {
        List<String> cats = e.getCategories() == null ? List.of()
                : e.getCategories().stream().map(Category::getName).collect(Collectors.toList());

        EventResponseDTO dto = EventResponseDTO.builder()
                .id(e.getId())
                .title(e.getTitle())
                .description(e.getDescription())
                .eventDate(e.getEventDate())
                .location(e.getLocation())
                .maxParticipants(e.getMaxParticipants())
                .availablePlaces(e.getAvailablePlaces())
                .hasAvailablePlaces(e.hasAvailablePlaces())
                .fillRate(Math.round(e.getFillRate() * 10.0) / 10.0)
                .categories(cats)
                .build();

        dto.add(linkTo(methodOn(EventController.class).getById(e.getId())).withSelfRel());
        dto.add(linkTo(methodOn(EventController.class).getAll(null, null)).withRel("events"));
        
        // Nouveaux liens hypermédias intelligents
        dto.add(linkTo(methodOn(ReviewController.class).getByEvent(e.getId())).withRel("reviews"));
        
        if (e.hasAvailablePlaces()) {
            dto.add(linkTo(methodOn(ReservationController.class).create(new ReservationRequestDTO())).withRel("reserve"));
        }
        
        return dto;
    }
}
