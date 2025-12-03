// src/pages/EventsPage.jsx
import React, { useEffect, useState } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import EventCard from "../componenets/EventCard";
import { searchApi } from "../api/api";

export default function EventsPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  const category = searchParams.get("category") || "";
  const city = searchParams.get("city") || localStorage.getItem("eventix_city") || "";
  const q = searchParams.get("q") || "";

  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const [page, setPage] = useState(Number(searchParams.get("page") || 0));
  const [limit] = useState(Number(searchParams.get("limit") || 10));

  useEffect(() => {
    const fetch = async () => {
      setLoading(true);
      setError(null);
      try {
        const params = {
          page,
          limit,
        };
        if (category) params.category = category;
        if (city) params.city = city;
        if (q) params.q = q;

        const res = await searchApi.get("/search/events", { params });
        const content = Array.isArray(res.data?.content) ? res.data.content : res.data || [];
        setEvents(content);
      } catch (err) {
        console.error("Search events error:", err);
        setError("Unable to load events");
        setEvents([]);
      } finally {
        setLoading(false);
      }
    };

    fetch();
    // keep URL in sync with page
    setSearchParams((p) => {
      const copy = new URLSearchParams(p.toString());
      copy.set("page", String(page));
      copy.set("limit", String(limit));
      if (category) copy.set("category", category);
      if (city) copy.set("city", city);
      if (q) copy.set("q", q);
      return copy;
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [category, city, q, page, limit]);

  const onEventClick = (ev) => navigate(`/events/${ev.id}`);

  return (
    <div className="container mx-auto px-6 py-8">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-xl font-semibold">Events {category ? `• ${category}` : ""}</h1>
        <div className="text-sm text-gray-600">{city ? `City: ${city}` : "All cities"}</div>
      </div>

      {loading ? (
        <div className="text-sm text-gray-500">Loading events...</div>
      ) : error ? (
        <div className="text-sm text-red-500">{error}</div>
      ) : events.length === 0 ? (
        <div className="text-sm text-gray-500">No events found</div>
      ) : (
        <div className="grid grid-cols-1 gap-4">
          {events.map((ev) => (
            <EventCard key={ev.id} event={ev} onClick={onEventClick} />
          ))}
        </div>
      )}

      {/* Simple pagination */}
      <div className="mt-6 flex items-center justify-between">
        <button
          onClick={() => setPage((p) => Math.max(0, p - 1))}
          disabled={page === 0}
          className="px-3 py-1 text-sm rounded border border-gray-200 disabled:opacity-50"
        >
          Prev
        </button>
        <div className="text-sm text-gray-600">Page {page + 1}</div>
        <button
          onClick={() => setPage((p) => p + 1)}
          className="px-3 py-1 text-sm rounded border border-gray-200"
        >
          Next
        </button>
      </div>
    </div>
  );
}
