package com.example.secureapihateoas.repository;

import com.example.secureapihateoas.entities.Event;
import com.example.secureapihateoas.entities.Reservation;
import com.example.secureapihateoas.entities.ReservationStatus;
import com.example.secureapihateoas.entities.Users;
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

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.event = :event AND r.status = 'CONFIRMED'")
    long countConfirmedReservationsByEvent(@Param("event") Event event);

    // ─── Dashboard global ─────────────────────────────────────────────────────
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.status = :status")
    long countByStatus(@Param("status") ReservationStatus status);

    // ─── Réservations par statut pour un événement ────────────────────────────
    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.event = :event AND r.status = :status")
    long countByEventAndStatusQuery(@Param("event") Event event, @Param("status") ReservationStatus status);

    // ─── Distribution des réservations par statut pour un événement ──────────
    @Query("SELECT r.status, COUNT(r) FROM Reservation r WHERE r.event = :event GROUP BY r.status")
    List<Object[]> countGroupedByStatusForEvent(@Param("event") Event event);

    // ─── Distribution globale par statut ─────────────────────────────────────
    @Query("SELECT r.status, COUNT(r) FROM Reservation r GROUP BY r.status")
    List<Object[]> countGroupedByStatus();
}
