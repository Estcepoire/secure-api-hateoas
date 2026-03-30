package com.example.secureapihateoas.service;

import com.example.secureapihateoas.controller.DashboardController;
import com.example.secureapihateoas.dto.DashboardDTO;
import com.example.secureapihateoas.dto.EventDashboardDTO;
import com.example.secureapihateoas.dto.TopEventDTO;
import com.example.secureapihateoas.entities.Event;
import com.example.secureapihateoas.entities.ReservationStatus;
import com.example.secureapihateoas.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired private EventRepository eventRepository;
    @Autowired private ReservationRepository reservationRepository;
    @Autowired private ReviewRepository reviewRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private UserRepository userRepository;

    
    public DashboardDTO getGlobalDashboard() {

        long totalUsers = userRepository.count();

        long totalEvents = eventRepository.count();
        long eventsWithAvailablePlaces = eventRepository.countEventsWithAvailablePlaces();

        List<Event> allEvents = eventRepository.findAll();
        double averageFillRate = allEvents.stream()
                .mapToDouble(Event::getFillRate)
                .average()
                .orElse(0.0);

        long totalReservations = reservationRepository.count();
        long confirmedReservations = reservationRepository.countByStatus(ReservationStatus.CONFIRMED);
        long cancelledReservations = reservationRepository.countByStatus(ReservationStatus.CANCELLED);
        long pendingReservations = reservationRepository.countByStatus(ReservationStatus.PENDING);

        Map<String, Long> reservationsByStatus = new LinkedHashMap<>();
        reservationsByStatus.put("CONFIRMED", confirmedReservations);
        reservationsByStatus.put("CANCELLED", cancelledReservations);
        reservationsByStatus.put("PENDING", pendingReservations);

        long totalReviews = reviewRepository.count();
        Double rawGlobalRating = reviewRepository.getGlobalAverageRating();
        double globalAverageRating = rawGlobalRating != null
                ? Math.round(rawGlobalRating * 10.0) / 10.0
                : 0.0;

        long totalCategories = categoryRepository.count();
        Map<String, Long> eventsByCategory = new LinkedHashMap<>();
        for (Object[] row : eventRepository.countEventsByCategory()) {
            eventsByCategory.put((String) row[0], (Long) row[1]);
        }

        List<TopEventDTO> topByFillRate = buildTopEventList(allEvents, true);

        List<TopEventDTO> topByRating = buildTopEventList(allEvents, false);

        DashboardDTO dto = DashboardDTO.builder()
                .totalUsers(totalUsers)
                .totalEvents(totalEvents)
                .eventsWithAvailablePlaces(eventsWithAvailablePlaces)
                .averageFillRate(Math.round(averageFillRate * 10.0) / 10.0)
                .totalReservations(totalReservations)
                .confirmedReservations(confirmedReservations)
                .cancelledReservations(cancelledReservations)
                .pendingReservations(pendingReservations)
                .reservationsByStatus(reservationsByStatus)
                .totalReviews(totalReviews)
                .globalAverageRating(globalAverageRating)
                .totalCategories(totalCategories)
                .eventsByCategory(eventsByCategory)
                .topEventsByFillRate(topByFillRate)
                .topEventsByRating(topByRating)
                .build();

        dto.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DashboardController.class).getGlobalDashboard())
                .withSelfRel());

        return dto;
    }

    
    public EventDashboardDTO getEventDashboard(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Événement introuvable : " + eventId));

        List<String> categories = event.getCategories()
                .stream()
                .map(c -> c.getName())
                .collect(Collectors.toList());

        long totalReservations = reservationRepository.findByEvent(event).size();
        long confirmed = reservationRepository.countByEventAndStatusQuery(event, ReservationStatus.CONFIRMED);
        long cancelled  = reservationRepository.countByEventAndStatusQuery(event, ReservationStatus.CANCELLED);
        long pending    = reservationRepository.countByEventAndStatusQuery(event, ReservationStatus.PENDING);

        Map<String, Long> reservationsByStatus = new LinkedHashMap<>();
        reservationsByStatus.put("CONFIRMED", confirmed);
        reservationsByStatus.put("CANCELLED", cancelled);
        reservationsByStatus.put("PENDING", pending);

        long totalReviews = reviewRepository.findByEvent(event).size();

        Double rawAvg = reviewRepository.getAverageRatingByEvent(event);
        double averageRating = rawAvg != null
                ? Math.round(rawAvg * 10.0) / 10.0
                : 0.0;

        Map<Integer, Long> ratingDistribution = new TreeMap<>();
        for (int i = 1; i <= 5; i++) ratingDistribution.put(i, 0L);
        for (Object[] row : reviewRepository.getRatingDistributionForEvent(event)) {
            ratingDistribution.put((Integer) row[0], (Long) row[1]);
        }

        long positiveReviews = reviewRepository.countPositiveReviewsByEvent(event);
        double positiveReviewRate = totalReviews > 0
                ? Math.round((double) positiveReviews / totalReviews * 1000.0) / 10.0
                : 0.0;

        EventDashboardDTO dto = EventDashboardDTO.builder()
                .eventId(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .location(event.getLocation())
                .eventDate(event.getEventDate())
                .categories(categories)
                .maxParticipants(event.getMaxParticipants())
                .availablePlaces(event.getAvailablePlaces())
                .fillRate(Math.round(event.getFillRate() * 10.0) / 10.0)
                .hasAvailablePlaces(event.hasAvailablePlaces())
                .totalReservations(totalReservations)
                .confirmedReservations(confirmed)
                .cancelledReservations(cancelled)
                .pendingReservations(pending)
                .reservationsByStatus(reservationsByStatus)
                .totalReviews(totalReviews)
                .averageRating(averageRating)
                .ratingDistribution(ratingDistribution)
                .positiveReviewRate(positiveReviewRate)
                .build();

        dto.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DashboardController.class).getEventDashboard(eventId))
                .withSelfRel());
        dto.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(DashboardController.class).getGlobalDashboard())
                .withRel("global-dashboard"));

        return dto;
    }

    private List<TopEventDTO> buildTopEventList(List<Event> allEvents, boolean byFillRate) {
        return allEvents.stream()
                .map(e -> {
                    Double rawAvg = reviewRepository.getAverageRatingByEvent(e);
                    double avg = rawAvg != null ? Math.round(rawAvg * 10.0) / 10.0 : 0.0;
                    long reviewCount = reviewRepository.findByEvent(e).size();
                    long confirmed = reservationRepository.countByEventAndStatusQuery(e, ReservationStatus.CONFIRMED);
                    return TopEventDTO.builder()
                            .eventId(e.getId())
                            .title(e.getTitle())
                            .location(e.getLocation())
                            .maxParticipants(e.getMaxParticipants())
                            .confirmedReservations(confirmed)
                            .fillRate(Math.round(e.getFillRate() * 10.0) / 10.0)
                            .averageRating(avg)
                            .reviewCount(reviewCount)
                            .build();
                })
                .sorted(byFillRate
                        ? Comparator.comparingDouble(TopEventDTO::getFillRate).reversed()
                        : Comparator.comparingDouble(TopEventDTO::getAverageRating).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }
}
