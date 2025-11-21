package com.eventix.booking.service;

import com.eventix.booking.clients.EventClient;
import com.eventix.booking.dto.EventResponse;
import com.eventix.booking.model.Booking;
import com.eventix.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository repository;
    private final NotificationPublisher notificationPublisher;

    @Autowired
    public BookingService(BookingRepository repository, NotificationPublisher notificationPublisher) {
        this.repository = repository;
        this.notificationPublisher = notificationPublisher;
    }

    // ---------- CRUD & async logic ----------

    public Booking createBooking(Booking booking) {

        EventResponse event = eventClient.getEvent(UUID.fromString(booking.getEventId()));
        if (event == null) {
            throw new RuntimeException("Event not found");
        }
        
        setDefaults(booking);
        booking.setStatus("CONFIRMED");
        Booking saved = repository.save(booking);

        // Publish notification to RabbitMQ
        publishBookingNotification(saved);

        return saved;
    }

    @Async
    public CompletableFuture<Booking> createBookingAsync(Booking booking) {
        setDefaults(booking);
        booking.setStatus("PENDING");
        Booking saved = repository.save(booking);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        saved.setStatus("CONFIRMED");
        Booking confirmed = repository.save(saved);

        // Publish notification to RabbitMQ after confirmation
        publishBookingNotification(confirmed);

        return CompletableFuture.completedFuture(confirmed);
    }

    public Optional<Booking> getBooking(String id) {
        return repository.findById(id);
    }

    public List<Booking> getAllBookings() {
        return repository.findAll();
    }

    public List<Booking> findAll() {
        return repository.findAll();
    }
    public List<Booking> getBookingsByUser(String userId) {
        return repository.findByUserId(userId);
    }

    public List<Booking> getBookingsByEventId(String eventId) {
        return repository.findByEventId(eventId);
    }

    public List<Booking> getBookingsByEventIds(List<String> eventIds) {
        return repository.findAll().stream()
                .filter(booking -> eventIds.contains(booking.getEventId()))
                .collect(Collectors.toList());
    }

    public Booking save(Booking booking) {
        return repository.save(booking);
    }

    public void deleteBooking(String id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Booking not found: " + id);
        }
        repository.deleteById(id);
    }

    // ---------- Stats & Recommendations ----------

    public List<Booking> trending(int limit) {
        return repository.findAll().stream()
                .sorted(Comparator.comparingInt(
                        (Booking b) -> Optional.ofNullable(b.getTotalBookings()).orElse(0)).reversed())
                .limit(limit)
                .toList();
    }

    public List<Booking> personalized(String userId, int limit) {
        if (userId != null && !userId.isBlank()) {
            List<Booking> byUser = repository.findByUserId(userId);
            if (!byUser.isEmpty()) {
                return byUser.stream().limit(limit).toList();
            }
        }
        return repository.findAll().stream().limit(limit).toList();
    }

    public Map<String, Object> stats() {
        List<Booking> all = repository.findAll();
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalRecords", all.size());
        stats.put("confirmed", all.stream()
                .filter(b -> "CONFIRMED".equalsIgnoreCase(b.getStatus()))
                .count());

        stats.put("avgRating", all.stream()
                .map(Booking::getAvgRating)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0));

        Map<String, Long> byCity = all.stream()
                .filter(b -> b.getCity() != null)
                .collect(Collectors.groupingBy(Booking::getCity, Collectors.counting()));

        stats.put("byCity", byCity);

        return stats;
    }

    // ---------- NEW: GraphQL helper for eventStats ----------

    public Map<String, Object> getEventStats(String eventId) {
        List<Booking> all = repository.findByEventId(eventId);

        int totalBookings = all.size();
        double avgRating = all.stream()
                .map(Booking::getAvgRating)
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0.0);

        long wishlistCount = all.stream()
                .filter(b -> Boolean.TRUE.equals(b.getWishlisted()))
                .count();

        List<Booking> recentBookings = all.stream()
                .sorted(Comparator.comparing(Booking::getTimestamp).reversed())
                .limit(5)
                .toList();

        String city = all.stream().map(Booking::getCity).filter(Objects::nonNull).findFirst().orElse("Unknown");
        String category = all.stream().map(Booking::getCategory).filter(Objects::nonNull).findFirst().orElse("General");
        String userId = all.stream().map(Booking::getUserId).filter(Objects::nonNull).findFirst().orElse("N/A");

        return Map.of(
                "eventId", eventId,
                "totalBookings", totalBookings,
                "recentBookings", recentBookings,
                "wishlistCount", wishlistCount,
                "avgRating", avgRating,
                "userId", userId,
                "timestamp", Instant.now(),
                "city", city,
                "category", category
        );
    }

    // ---------- Helper ----------

    private void setDefaults(Booking booking) {
        if (booking.getTimestamp() == null) {
            booking.setTimestamp(Instant.now());
        }
        if (booking.getStatus() == null) {
            booking.setStatus("NEW");
        }
    }

    /**
     * Publishes a booking notification to RabbitMQ.
     * Uses placeholder values for userEmail, userName, and eventName since they're not in the Booking model.
     * In a real application, you would fetch these from user-service and event-service.
     */
    private void publishBookingNotification(Booking booking) {
        // TODO: In production, fetch actual user email and name from user-service
        // TODO: In production, fetch actual event name from event-service

        String userEmail = booking.getUserId() != null ? booking.getUserId() + "@example.com" : "user@example.com";
        String userName = "User " + (booking.getUserId() != null ? booking.getUserId() : "Unknown");
        String eventName = "Event " + (booking.getEventId() != null ? booking.getEventId() : "Unknown");

        notificationPublisher.publishBookingNotification(
            userEmail,
            userName,
            eventName,
            booking.getId()
        );
    }
}
