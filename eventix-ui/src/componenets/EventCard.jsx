// src/components/EventCard.jsx
import React from "react";

export default function EventCard({ event, onClick }) {
  return (
    <div
      onClick={() => onClick?.(event)}
      className="cursor-pointer bg-white rounded-xl shadow-sm hover:shadow-md transition p-3"
    >
      {/* Thumbnail placeholder */}
      <div className="w-full h-36 rounded-lg bg-gradient-to-br from-purple-100 to-white flex items-center justify-center text-purple-600 font-semibold">
        {(event.title || "")
          .split(" ")
          .slice(0, 2)
          .map((s) => s[0])
          .join("")}
      </div>

      {/* Title */}
      <h3 className="mt-3 text-sm font-bold text-gray-900 line-clamp-2">
        {event.title}
      </h3>

      {/* Venue & City */}
      <p className="text-xs text-gray-500 mt-1">
        {event.venue} • {event.city}
      </p>

      {/* Category */}
      <p className="text-xs text-purple-600 font-medium mt-2">
        {event.category}
      </p>

      {/* Seats */}
      <div className="mt-3 text-xs text-gray-700">
        {event.seatsAvailable}/{event.capacity} seats left
      </div>
    </div>
  );
}
