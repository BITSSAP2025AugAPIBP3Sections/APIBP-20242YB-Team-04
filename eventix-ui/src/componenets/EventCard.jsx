// src/components/EventCard.jsx
import React from "react";
import { FiMapPin } from "react-icons/fi";

export default function EventCard({ event, onClick, isBooked = false }) {
  const initials =
    (event.title || "")
      .split(" ")
      .filter(Boolean)
      .slice(0, 2)
      .map((s) => s[0]?.toUpperCase())
      .join("") || "EV";

  return (
    <div
      onClick={() => onClick(event)}
      className="relative cursor-pointer rounded-2xl border border-white/10 bg-white/5 p-4 shadow-[0_18px_35px_rgba(0,0,0,0.6)] backdrop-blur-sm transition transform hover:-translate-y-1 hover:shadow-[0_24px_50px_rgba(0,0,0,0.8)]"
    >
      {/* BOOKED BADGE */}
      {isBooked && (
        <div className="absolute top-2 right-2 rounded-full bg-green-500 shadow-lg shadow-green-500/40 p-1.5">
          <svg
            className="h-3.5 w-3.5 text-white"
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

      {/* Thumbnail / Avatar */}
      <div className="mb-3 w-full h-32 rounded-xl bg-gradient-to-br from-purple-800/70 via-purple-600/40 to-indigo-600/60 flex items-center justify-center text-lg font-semibold text-purple-50 tracking-wide">
        {initials}
      </div>

      {/* Content */}
      <h3 className="text-[0.98rem] font-semibold text-white line-clamp-2">
        {event.title}
      </h3>

      <p className="mt-1 text-xs font-medium uppercase tracking-[0.18em] text-purple-200">
        {event.category}
      </p>

      <div className="mt-2 flex items-center gap-1 text-xs text-gray-200">
        <FiMapPin size={14} className="text-purple-300" />
        <span className="truncate">{event.city}</span>
      </div>

      {event.seatsAvailable !== undefined && event.seatsAvailable !== null && (
        <div className="mt-2 text-[11px] text-gray-300">
          Seats available:{" "}
          <span
            className={
              event.seatsAvailable > 0
                ? "font-semibold text-green-300"
                : "font-semibold text-red-300"
            }
          >
            {event.seatsAvailable}
          </span>
        </div>
      )}
    </div>
  );
}
