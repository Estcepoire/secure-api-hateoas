package com.example.secureapihateoas.service;

import com.example.secureapihateoas.controller.UserController;
import com.example.secureapihateoas.dto.UserResponseDTO;
import com.example.secureapihateoas.entities.Users;
import com.example.secureapihateoas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;

    // ─── GET ALL ──────────────────────────────────────────────────────────────
    public List<UserResponseDTO> getAll() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ─── GET BY ID ────────────────────────────────────────────────────────────
    public UserResponseDTO getById(Long id) {
        return toDTO(findOrThrow(id));
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────
    public UserResponseDTO update(Long id, String name, String role) {
        Users user = findOrThrow(id);
        if (name != null && !name.isBlank()) user.setName(name);
        if (role != null && !role.isBlank()) user.setRole(role);
        return toDTO(userRepository.save(user));
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────
    public void delete(Long id) {
        findOrThrow(id);
        userRepository.deleteById(id);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────
    private Users findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utilisateur introuvable : " + id));
    }

    public UserResponseDTO toDTO(Users u) {
        UserResponseDTO dto = UserResponseDTO.builder()
                .id(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                .role(u.getRole())
                .reservationCount(u.getReservations() == null ? 0 : u.getReservations().size())
                .reviewCount(u.getReviews() == null ? 0 : u.getReviews().size())
                .build();

        dto.add(linkTo(methodOn(UserController.class).getById(u.getId())).withSelfRel());
        dto.add(linkTo(methodOn(UserController.class).getAll()).withRel("users"));
        return dto;
    }
}
