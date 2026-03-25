package com.example.secureapihateoas.repository;

import com.example.secureapihateoas.entities.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e join e.categories c where c.id = :categoryId")
    List<Event> findByCategoryId(@Param("categoryId") Long categoryId);

    List<Event> findByEventDateBetween(LocalDateTime start, LocalDateTime end);

    List<Event> findByLocationContainingIgnoreCase(String location);

    @Query("select e from Event e where lower(e.title) like lower(CONCAT('%', :keyword, '%')) or lower(e.description) like lower(CONCAT('%', :keyword, '%'))")
    List<Event> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT e FROM Event e WHERE e.maxParticipants > (SELECT COUNT(r) FROM Reservation r WHERE r.event = e AND r.status = 'CONFIRMED')")
    List<Event> findEventsWithAvailablePlaces();
}
