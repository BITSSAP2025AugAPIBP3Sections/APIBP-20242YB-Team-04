// src/pages/MyBookings.jsx
import React, { useEffect, useState } from "react";
import axios from "axios";
import { useSelector } from "react-redux";
import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

export default function MyBookings() {
  const user = useSelector((st) => st.userAuth?.token || null);
  const userId = user?.userId?.toString?.() || null;
  const navigate = useNavigate();

  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchBookings = async () => {
    if (!userId) {
      setBookings([]);
      return;
    }
    setLoading(true);
    setError(null);
    try {
      const accessToken =
        localStorage.getItem("accessToken") || localStorage.getItem("token");
      const headers = accessToken
        ? { Authorization: `Bearer ${accessToken}` }
        : {};
      const url = `http://localhost:8082/bookings?userId=${encodeURIComponent(
        userId
      )}`;
      const res = await axios.get(url, { headers, timeout: 8000 });
      const list = Array.isArray(res.data) ? res.data : [];
      setBookings(list);
    } catch (err) {
      console.error("Fetch bookings error:", err);
      setError("Unable to load bookings. Try again.");
      toast.error("Unable to load bookings");
      setBookings([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBookings();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [userId]);

  return (
    <div className="min-h-screen py-10 px-4">
      <div className="container mx-auto max-w-4xl">
        <div className="mb-6">
          <h1 className="text-2xl font-semibold text-white">My Bookings</h1>
          <p className="mt-1 text-sm text-gray-300">
            All your confirmed and recent bookings in one place.
          </p>
        </div>

        {loading ? (
          <div className="text-sm text-gray-300">Loading bookings...</div>
        ) : error ? (
          <div className="text-sm text-red-400">{error}</div>
        ) : bookings.length === 0 ? (
          <div className="rounded-2xl border border-white/10 bg-white/5 p-5 text-sm text-gray-200 backdrop-blur">
            You have no bookings yet. Discover events and start contributing to
            causes that matter.
          </div>
        ) : (
          <div className="space-y-4">
            {bookings.map((b) => {
              const status = b.status || "";
              const isConfirmed = status.startsWith("CONFIRMED");
              const isFailed = status.startsWith("FAILED");

              const statusColor = isConfirmed
                ? "text-green-300"
                : isFailed
                ? "text-red-300"
                : "text-gray-200";

              return (
                <div
                  key={b.id}
                  className="flex items-center justify-between rounded-2xl border border-white/10 bg-white/5 p-4 text-sm text-gray-100 shadow-[0_18px_35px_rgba(0,0,0,0.6)] backdrop-blur-sm"
                >
                  <div>
                    <div className="text-sm font-semibold text-white">
                      Event ID: {b.eventId}
                    </div>
                    <div className="mt-1 text-xs text-gray-300">
                      {b.city} • {b.category}
                    </div>
                    <div className="mt-1 text-xs text-gray-400">
                      Booked: {new Date(b.timestamp).toLocaleString()}
                    </div>
                  </div>

                  <div className="flex flex-col items-end gap-2">
                    <div className={`text-xs font-semibold ${statusColor}`}>
                      {status || "PENDING"}
                    </div>
                    <div className="text-[11px] text-gray-300">
                      Seats:{" "}
                      <span className="font-semibold text-gray-100">
                        {b.totalBookings}
                      </span>
                    </div>

                    <div className="mt-2 flex gap-2">
                      <button
                        onClick={() => navigate(`/events/${b.eventId}`)}
                        className="rounded-full border border-white/20 px-3 py-1 text-[11px] text-gray-100 hover:bg-white/10"
                      >
                        View event
                      </button>

                      {isConfirmed && (
                        <div className="rounded-full bg-green-500/20 px-3 py-1 text-[11px] font-semibold text-green-200">
                          Booked
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}

        <div className="mt-6">
          <button
            onClick={fetchBookings}
            className="rounded-full bg-purple-500 px-4 py-2 text-sm font-semibold text-white shadow-md shadow-purple-500/40 hover:bg-purple-400"
          >
            Refresh
          </button>
        </div>
      </div>

      <ToastContainer position="top-center" autoClose={2000} />
    </div>
  );
}
