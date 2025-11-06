package com.eventix.booking.graphql;

import com.eventix.booking.model.Booking;
import com.eventix.booking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class BookingMutationResolver {

    private final BookingService svc;

    @Autowired
    public BookingMutationResolver(BookingService svc) {
        this.svc = svc;
    }

    @MutationMapping
    public Booking createBooking(String eventId, String userId, String city, String category,
                                 Double avgRating, Integer wishlistCount) {

        Booking booking = new Booking();
        booking.setEventId(eventId);
        booking.setUserId(userId);
        booking.setCity(city);
        booking.setCategory(category);
        booking.setAvgRating(avgRating);
        booking.setWishlistCount(wishlistCount);
        booking.setStatus("CONFIRMED");

        return svc.save(booking);
    }
}
