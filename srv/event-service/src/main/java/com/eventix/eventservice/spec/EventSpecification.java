package com.eventix.eventservice.spec;

import com.eventix.eventservice.model.Event;
import com.eventix.eventservice.model.EventStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public final class EventSpecification {

    public static Specification<Event> hasCity(String city) {
        return (root, query, cb) -> city == null ? null : cb.equal(cb.lower(root.get("city")), city.toLowerCase());
    }

    public static Specification<Event> hasCategory(String category) {
        return (root, query, cb) -> category == null ? null : cb.equal(cb.lower(root.get("category")), category.toLowerCase());
    }

    public static Specification<Event> isPublished() {
        return (root, query, cb) -> cb.equal(root.get("status"), EventStatus.PUBLISHED);
    }

    public static Specification<Event> startsAfter(ZonedDateTime from) {
        return (root, query, cb) -> from == null ? null : cb.greaterThanOrEqualTo(root.get("startTime"), from);
    }

    public static Specification<Event> endsBefore(ZonedDateTime to) {
        return (root, query, cb) -> to == null ? null : cb.lessThanOrEqualTo(root.get("endTime"), to);
    }

    public static Specification<Event> hasOrganizer(String organizerId) {
        return (root, query, cb) -> organizerId == null ? null : cb.equal(root.get("organizerId"), organizerId);
    }
}
