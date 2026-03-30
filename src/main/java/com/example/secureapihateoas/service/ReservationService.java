package com.example.secureapihateoas.service;

import com.example.secureapihateoas.controller.ReservationController;
import com.example.secureapihateoas.dto.ReservationRequestDTO;
import com.example.secureapihateoas.dto.ReservationResponseDTO;
import com.example.secureapihateoas.entities.*;
import com.example.secureapihateoas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class ReservationService {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private EventRepository eventRepository;
    @Autowired private UserRepository userRepository;

    // ─── GET ALL ──────────────────────────────────────────────────────────────
    public List<ReservationResponseDTO> getAll() {
        return reservationRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ─── GET BY ID ────────────────────────────────────────────────────────────
    public ReservationResponseDTO getById(Long id) {
        return toDTO(findOrThrow(id));
    }

    // ─── GET BY EVENT ─────────────────────────────────────────────────────────
    public List<ReservationResponseDTO> getByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Événement introuvable : " + eventId));
        return reservationRepository.findByEvent(event).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ─── GET BY USER ──────────────────────────────────────────────────────────
    public List<ReservationResponseDTO> getByUser(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utilisateur introuvable : " + userId));
        return reservationRepository.findByUser(user).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────
    public ReservationResponseDTO create(ReservationRequestDTO dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Événement introuvable : " + dto.getEventId()));

        if (!event.hasAvailablePlaces()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Plus de places disponibles pour cet événement");
        }

        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utilisateur introuvable : " + dto.getUserId()));

        Reservation reservation = Reservation.builder()
                .event(event)
                .user(user)
                .reservationDate(LocalDateTime.now())
                .status(dto.getStatus() != null ? dto.getStatus() : ReservationStatus.CONFIRMED)
                .build();

        return toDTO(reservationRepository.save(reservation));
    }

    // ─── UPDATE STATUS ────────────────────────────────────────────────────────
    public ReservationResponseDTO updateStatus(Long id, ReservationStatus newStatus) {
        Reservation reservation = findOrThrow(id);
        reservation.setStatus(newStatus);
        return toDTO(reservationRepository.save(reservation));
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────
    public void delete(Long id) {
        findOrThrow(id);
        reservationRepository.deleteById(id);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────
    private Reservation findOrThrow(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Réservation introuvable : " + id));
    }

    public ReservationResponseDTO toDTO(Reservation r) {
        ReservationResponseDTO dto = ReservationResponseDTO.builder()
                .id(r.getId())
                .eventId(r.getEvent().getId())
                .eventTitle(r.getEvent().getTitle())
                .userId(r.getUser().getId())
                .userName(r.getUser().getName())
                .reservationDate(r.getReservationDate())
                .status(r.getStatus())
                .build();

        dto.add(linkTo(methodOn(ReservationController.class).getById(r.getId())).withSelfRel());
        dto.add(linkTo(methodOn(ReservationController.class).getAll()).withRel("reservations"));
        return dto;
    }
}
