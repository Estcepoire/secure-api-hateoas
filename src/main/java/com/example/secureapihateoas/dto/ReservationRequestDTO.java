package com.example.secureapihateoas.dto;

import com.example.secureapihateoas.entities.ReservationStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ReservationRequestDTO {

    @NotNull(message = "L'identifiant de l'événement est obligatoire")
    private Long eventId;

    @NotNull(message = "L'identifiant de l'utilisateur est obligatoire")
    private Long userId;

    private ReservationStatus status = ReservationStatus.CONFIRMED;
}
