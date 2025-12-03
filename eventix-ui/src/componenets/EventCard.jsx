// src/components/EventCard.jsx
import React from "react";
import { FiMapPin } from "react-icons/fi";

export default function EventCard({ event, onClick, isBooked = false }) {
  return (
    <div
      onClick={() => onClick(event)}
      className="relative bg-white rounded-xl shadow hover:shadow-lg p-4 cursor-pointer transition"
    >
      {/* BOOKED BADGE */}
      {isBooked && (
        <div className="absolute top-2 right-2 bg-green-600 rounded-full p-1 shadow-lg">
          <svg
            className="w-4 h-4 text-white"
            fill="none"
            stroke="currentColor"
            strokeWidth="3"
            viewBox="0 0 24 24"
          >
            <path
              d="M20 6L9 17l-5-5"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
        </div>
      )}
      {/* Thumbnail placeholder */}
      <div className="w-full h-36 rounded-lg bg-gradient-to-br from-purple-100 to-white flex items-center justify-center text-purple-600 font-semibold">
        {(event.title || "")
          .split(" ")
          .slice(0, 2)
          .map((s) => s[0])
          .join("")}
      </div>

      <h3 className="text-lg font-semibold text-gray-900">{event.title}</h3>
      <p className="text-sm text-gray-500 mt-1">{event.category}</p>

      <div className="flex items-center gap-1 text-gray-600 text-sm mt-2">
        <FiMapPin size={14} />
        {event.city}
      </div>

      {event.seatsAvailable ? (
        <div className="text-xs text-gray-500 mt-2">
          Seats Available: {event.seatsAvailable}
        </div>
      ) : null}
    </div>
  );
}
