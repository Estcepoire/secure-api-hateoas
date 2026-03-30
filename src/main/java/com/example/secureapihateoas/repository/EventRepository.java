package com.example.secureapihateoas.repository;

import com.example.secureapihateoas.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e JOIN e.categories c WHERE c.id = :categoryId")
    List<Event> findByCategoryId(@Param("categoryId") Long categoryId);

    List<Event> findByEventDateBetween(LocalDateTime start, LocalDateTime end);

    List<Event> findByLocationContainingIgnoreCase(String location);

    @Query("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Event> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT e FROM Event e WHERE e.maxParticipants > (SELECT COUNT(r) FROM Reservation r WHERE r.event = e AND r.status = 'CONFIRMED')")
    List<Event> findEventsWithAvailablePlaces();

    // ─── Dashboard global ─────────────────────────────────────────────────────
    @Query("SELECT COUNT(e) FROM Event e WHERE e.maxParticipants > (SELECT COUNT(r) FROM Reservation r WHERE r.event = e AND r.status = 'CONFIRMED')")
    long countEventsWithAvailablePlaces();

    /** Nombre d'événements par catégorie : [categoryName, count] */
    @Query("SELECT c.name, COUNT(e) FROM Event e JOIN e.categories c GROUP BY c.name ORDER BY COUNT(e) DESC")
    List<Object[]> countEventsByCategory();
}
