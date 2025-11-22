package com.eventix.booking.graphql;

import com.eventix.booking.dto.StatsResponse;
import com.eventix.booking.model.Booking;
import com.eventix.booking.service.BookingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class BookingQueryResolver {

    private final BookingService svc;

    @Autowired
    public BookingQueryResolver(BookingService svc) {
        this.svc = svc;
    }

    // --------------------------------------------
    // 1. ALL BOOKINGS
    // --------------------------------------------
    @QueryMapping
    public List<Booking> allBookings() {
        return svc.findAll();
    }

    // --------------------------------------------
    // 2. TRENDING BOOKINGS
    // --------------------------------------------
    @QueryMapping
    public List<Booking> trending(@Argument Integer limit) {
        int safeLimit = (limit == null || limit <= 0) ? 10 : limit;
        return svc.trending(safeLimit);
    }

    // --------------------------------------------
    // 3. PERSONALIZED BOOKINGS
    // --------------------------------------------
    @QueryMapping
    public List<Booking> personalized(
            @Argument String userId,
            @Argument Integer limit
    ) {
        int safeLimit = (limit == null || limit <= 0) ? 10 : limit;
        return svc.personalized(userId, safeLimit);
    }

    // --------------------------------------------
    // 4. STATS
    // --------------------------------------------
    @QueryMapping
    public StatsResponse stats() {

        Map<String, Object> map = svc.stats();

        // Convert byCity map to list format
        List<StatsResponse.CityCount> cityCounts =
                ((Map<String, Long>) map.get("byCity"))
                .entrySet()
                .stream()
                .map(entry -> new StatsResponse.CityCount(entry.getKey(), entry.getValue().intValue()))
                .collect(Collectors.toList());

        StatsResponse stats = new StatsResponse();
        stats.setTotalRecords((Integer) map.get("totalRecords"));
        stats.setConfirmed(((Long) map.get("confirmed")).intValue());
        stats.setAvgRating((Double) map.get("avgRating"));
        stats.setByCity(cityCounts);

        return stats;
    }
}
