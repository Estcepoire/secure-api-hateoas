package com.example.secureapihateoas.dto;

import com.example.secureapihateoas.entities.ReservationStatus;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ReservationResponseDTO extends RepresentationModel<ReservationResponseDTO> {

    private Long id;
    private Long eventId;
    private String eventTitle;
    private Long userId;
    private String userName;
    private LocalDateTime reservationDate;
    private ReservationStatus status;
}
