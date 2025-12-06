// src/pages/EventsPage.jsx
import React, { useEffect, useState, useCallback } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";
import { eventsApi } from "../api/api"; // axios instance
import EventCard from "../componenets/EventCard";

export default function EventsPage() {
  const [searchParams, setSearchParams] = useSearchParams();
  const navigate = useNavigate();

  // query params
  const qParam = searchParams.get("q") || "";
  const categoryParam = searchParams.get("category") || "";
  const cityParamUrl = searchParams.get("city") || "";
  const pageParam = Number(searchParams.get("page") || 0);
  const limitParam = Number(searchParams.get("limit") || 10);
  const sortByParam = searchParams.get("sortBy") || ""; // e.g. 'popularity'

  const [events, setEvents] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(pageParam);
  const [limit, setLimit] = useState(limitParam);

  // keep search params synced when page/limit changes
  useEffect(() => {
    const p = new URLSearchParams(searchParams.toString());
    p.set("page", String(page));
    p.set("limit", String(limit));
    setSearchParams(p, { replace: true });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page, limit]);

  const fetchEvents = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const params = {
        page,
        limit,
      };

      if (qParam) params.q = qParam;
      if (categoryParam) params.category = categoryParam;
      // prefer explicit city in url, otherwise fallback to localStorage
      const city = cityParamUrl || localStorage.getItem("eventix_city") || "";
      if (city) params.city = city;
      if (sortByParam) params.sortBy = sortByParam;

      const res = await eventsApi.get("/events/search", { params });

      // defensive parsing
      let content = [];
      if (Array.isArray(res.data?.content)) content = res.data.content;
      else if (Array.isArray(res.data)) content = res.data;
      else if (Array.isArray(res.data?.events)) content = res.data.events;
      else content = [];

      setEvents(content);
    } catch (err) {
      console.error("Failed to fetch events:", err);
      setError("Unable to load events. Please try again.");
      setEvents([]);
    } finally {
      setLoading(false);
    }
  }, [qParam, categoryParam, cityParamUrl, page, limit, sortByParam, setSearchParams]);

  useEffect(() => {
    fetchEvents();
  }, [fetchEvents]);

  const onEventClick = (ev) => {
    navigate(`/events/${ev.id}`);
  };

  // helper to go to same page with a different category (if you want programmatic navigation)
  const goToCategory = (cat) => {
    const p = new URLSearchParams(searchParams.toString());
    p.set("category", cat);
    p.set("page", "0");
    setSearchParams(p);
  };

  const effectiveCity = cityParamUrl || localStorage.getItem("eventix_city") || "";

  return (
    <div className="min-h-screen py-8 px-4">
      <div className="container mx-auto max-w-6xl">
        <div className="rounded-3xl border border-white/10 bg-white/5 p-5 md:p-6 shadow-[0_18px_45px_rgba(0,0,0,0.6)] backdrop-blur">
          <div className="mb-6 flex flex-wrap items-center justify-between gap-3">
            <div>
              <h1 className="text-2xl font-semibold text-white">
                {categoryParam ? `Events • ${categoryParam}` : "Events"}
              </h1>
              {qParam && (
                <p className="mt-1 text-xs text-gray-300">
                  Showing results for{" "}
                  <span className="font-semibold text-purple-200">“{qParam}”</span>
                </p>
              )}
            </div>

            <div className="text-xs md:text-sm text-gray-300">
              {effectiveCity ? `City: ${effectiveCity}` : "All cities"}
            </div>
          </div>

          {loading ? (
            <div className="text-sm text-gray-300">Loading events...</div>
          ) : error ? (
            <div className="text-sm text-red-300">{error}</div>
          ) : events.length === 0 ? (
            <div className="rounded-2xl border border-white/10 bg-white/5 p-4 text-sm text-gray-200">
              No events found. Try adjusting your search or filters.
            </div>
          ) : (
            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
              {events.map((ev) => (
                <EventCard key={ev.id} event={ev} onClick={onEventClick} />
              ))}
            </div>
          )}

          {/* Pagination */}
          <div className="mt-6 flex items-center justify-between">
            <div>
              <button
                onClick={() => setPage((p) => Math.max(0, p - 1))}
                disabled={page === 0}
                className="rounded-full border border-white/25 px-3 py-1 text-sm text-gray-100 disabled:opacity-40 hover:bg-white/10"
              >
                Prev
              </button>
            </div>

            <div className="text-sm text-gray-300">Page {page + 1}</div>

            <div className="flex items-center gap-2">
              <select
                value={limit}
                onChange={(e) => {
                  setLimit(Number(e.target.value));
                  setPage(0);
                }}
                className="rounded-full border border-white/25 bg-black/30 px-2 py-1 text-sm text-gray-100 outline-none focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
              >
                <option value={6}>6</option>
                <option value={10}>10</option>
                <option value={20}>20</option>
              </select>

              <button
                onClick={() => setPage((p) => p + 1)}
                className="rounded-full border border-white/25 px-3 py-1 text-sm text-gray-100 hover:bg-white/10"
              >
                Next
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
