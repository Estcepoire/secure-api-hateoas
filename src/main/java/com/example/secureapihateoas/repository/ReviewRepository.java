package com.example.secureapihateoas.repository;

import com.example.secureapihateoas.entities.Event;
import com.example.secureapihateoas.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEvent(Event event);
    @Query("select avg(r.rating) from Review r where r.event = :event")
    Double getAverageRatingByEvent(@Param("event") Event event);
    List<Review> findByEventOrderByCreatedAtDesc(Event event);
}
