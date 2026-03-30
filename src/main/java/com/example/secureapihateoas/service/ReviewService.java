package com.example.secureapihateoas.service;

import com.example.secureapihateoas.controller.EventController;
import com.example.secureapihateoas.controller.ReviewController;
import com.example.secureapihateoas.controller.UserController;
import com.example.secureapihateoas.dto.ReviewRequestDTO;
import com.example.secureapihateoas.dto.ReviewResponseDTO;
import com.example.secureapihateoas.entities.*;
import com.example.secureapihateoas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Service
public class ReviewService {

    @Autowired private ReviewRepository reviewRepository;
    @Autowired private EventRepository eventRepository;
    @Autowired private UserRepository userRepository;

    public List<ReviewResponseDTO> getAll() {
        return reviewRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ReviewResponseDTO getById(Long id) {
        return toDTO(findOrThrow(id));
    }

    public List<ReviewResponseDTO> getByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Événement introuvable : " + eventId));
        return reviewRepository.findByEventOrderByCreatedAtDesc(event).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ReviewResponseDTO create(ReviewRequestDTO dto) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Événement introuvable : " + dto.getEventId()));

        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Utilisateur introuvable : " + dto.getUserId()));

        Review review = Review.builder()
                .event(event)
                .user(user)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        return toDTO(reviewRepository.save(review));
    }

    public ReviewResponseDTO update(Long id, ReviewRequestDTO dto) {
        Review review = findOrThrow(id);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        return toDTO(reviewRepository.save(review));
    }

    public void delete(Long id) {
        findOrThrow(id);
        reviewRepository.deleteById(id);
    }

    private Review findOrThrow(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Avis introuvable : " + id));
    }

    public ReviewResponseDTO toDTO(Review r) {
        ReviewResponseDTO dto = ReviewResponseDTO.builder()
                .id(r.getId())
                .eventId(r.getEvent().getId())
                .eventTitle(r.getEvent().getTitle())
                .userId(r.getUser().getId())
                .userName(r.getUser().getName())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build();

        dto.add(linkTo(methodOn(ReviewController.class).getById(r.getId())).withSelfRel());
        dto.add(linkTo(methodOn(ReviewController.class).getAll()).withRel("reviews"));
        
        dto.add(linkTo(methodOn(UserController.class).getById(r.getUser().getId())).withRel("user"));
        dto.add(linkTo(methodOn(EventController.class).getById(r.getEvent().getId())).withRel("event"));
        
        return dto;
    }
}
