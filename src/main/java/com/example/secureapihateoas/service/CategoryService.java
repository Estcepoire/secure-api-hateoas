package com.example.secureapihateoas.service;

import com.example.secureapihateoas.controller.CategoryController;
import com.example.secureapihateoas.controller.EventController;
import com.example.secureapihateoas.dto.CategoryRequestDTO;
import com.example.secureapihateoas.dto.CategoryResponseDTO;
import com.example.secureapihateoas.entities.Category;
import com.example.secureapihateoas.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class CategoryService {

    @Autowired private CategoryRepository categoryRepository;

    // ─── GET ALL ──────────────────────────────────────────────────────────────
    public List<CategoryResponseDTO> getAll() {
        return categoryRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ─── GET BY ID ────────────────────────────────────────────────────────────
    public CategoryResponseDTO getById(Long id) {
        return toDTO(findOrThrow(id));
    }

    // ─── CREATE ───────────────────────────────────────────────────────────────
    public CategoryResponseDTO create(CategoryRequestDTO dto) {
        Category cat = Category.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
        return toDTO(categoryRepository.save(cat));
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────
    public CategoryResponseDTO update(Long id, CategoryRequestDTO dto) {
        Category cat = findOrThrow(id);
        cat.setName(dto.getName());
        cat.setDescription(dto.getDescription());
        return toDTO(categoryRepository.save(cat));
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────
    public void delete(Long id) {
        findOrThrow(id);
        categoryRepository.deleteById(id);
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────
    private Category findOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Catégorie introuvable : " + id));
    }

    public CategoryResponseDTO toDTO(Category c) {
        CategoryResponseDTO dto = CategoryResponseDTO.builder()
                .id(c.getId())
                .name(c.getName())
                .description(c.getDescription())
                .eventCount(c.getEvents() == null ? 0 : c.getEvents().size())
                .build();

        dto.add(linkTo(methodOn(CategoryController.class).getById(c.getId())).withSelfRel());
        dto.add(linkTo(methodOn(CategoryController.class).getAll()).withRel("categories"));
        dto.add(linkTo(methodOn(EventController.class).getAll(c.getId(), null)).withRel("events"));
        
        return dto;
    }
}
