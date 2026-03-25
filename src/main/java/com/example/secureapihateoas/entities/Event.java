package com.example.secureapihateoas.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(nullable = false)
    private String location;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "event_categories",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    public int getAvailablePlaces(){
        if(reservations == null){
            return maxParticipants;
        }
        long confirmedReservations = reservations.stream().filter(r -> r.getStatus() == ReservationStatus.CONFIRMED).count();
        return maxParticipants - (int) confirmedReservations;
    }

    public boolean hasAvailablePlaces(){
        return getAvailablePlaces() > 0;
    }

    public double getFillRate(){
        if (maxParticipants == 0) return 0;
        long confirmedReservations = reservations.stream().filter(r -> r.getStatus() == ReservationStatus.CONFIRMED).count();
        return (double) confirmedReservations /maxParticipants * 100;
    }
}
