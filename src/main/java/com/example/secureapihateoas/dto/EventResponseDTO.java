package com.example.secureapihateoas.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class EventResponseDTO extends RepresentationModel<EventResponseDTO> {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime eventDate;
    private String location;
    private Integer maxParticipants;
    private int availablePlaces;
    private boolean hasAvailablePlaces;
    private double fillRate;
    private List<String> categories;
}
