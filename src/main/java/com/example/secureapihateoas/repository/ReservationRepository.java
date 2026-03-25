package com.example.secureapihateoas.repository;

import com.example.secureapihateoas.entities.Event;
import com.example.secureapihateoas.entities.Reservation;
import com.example.secureapihateoas.entities.ReservationStatus;
import com.example.secureapihateoas.entities.Users;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(Users user);
    List<Reservation> findByEvent(Event event);
    List<Reservation> findByUserAndStatus(Users user, ReservationStatus status);
    Optional<Reservation> findByUserAndEvent(Users user, Event event);
    long countByEventAndStatus(Event event, ReservationStatus status);
    @Query("select count(r) from Reservation r where r.event= :event and r.status= 'CONFIRMED'")
    long countConfirmedReservationsByEvent(@Param("event") Event event);
}
