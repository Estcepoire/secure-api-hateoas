package com.example.secureapihateoas.repository;

import com.example.secureapihateoas.entities.Event;
import com.example.secureapihateoas.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByEvent(Event event);

    List<Review> findByEventOrderByCreatedAtDesc(Event event);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.event = :event")
    Double getAverageRatingByEvent(@Param("event") Event event);

    @Query("SELECT AVG(r.rating) FROM Review r")
    Double getGlobalAverageRating();

    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.event = :event GROUP BY r.rating ORDER BY r.rating")
    List<Object[]> getRatingDistributionForEvent(@Param("event") Event event);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.event = :event AND r.rating >= 4")
    long countPositiveReviewsByEvent(@Param("event") Event event);
}
