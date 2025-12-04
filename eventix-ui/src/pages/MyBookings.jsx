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
      const accessToken = localStorage.getItem("accessToken") || localStorage.getItem("token");
      const headers = accessToken ? { Authorization: `Bearer ${accessToken}` } : {};
      const url = `http://localhost:8082/bookings?userId=${encodeURIComponent(userId)}`;
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
    // optionally poll or refresh on focus
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [userId]);

  return (
    <div className="min-h-screen bg-gray-50 py-10 px-4">
      <div className="container mx-auto max-w-4xl">
        <h1 className="text-2xl font-semibold mb-6">My Bookings</h1>

        {loading ? (
          <div className="text-gray-600">Loading bookings...</div>
        ) : error ? (
          <div className="text-red-600">{error}</div>
        ) : bookings.length === 0 ? (
          <div className="text-gray-600">You have no bookings yet.</div>
        ) : (
          <div className="space-y-4">
            {bookings.map((b) => (
              <div key={b.id} className="bg-white rounded-lg p-4 shadow-sm flex items-center justify-between">
                <div>
                  <div className="text-sm font-semibold">{/* You could fetch event title if you want */}Event ID: {b.eventId}</div>
                  <div className="text-xs text-gray-500 mt-1">{b.city} • {b.category}</div>
                  <div className="text-xs text-gray-500 mt-1">Booked: {new Date(b.timestamp).toLocaleString()}</div>
                </div>

                <div className="flex flex-col items-end gap-2">
                  <div className={`text-sm font-semibold ${b.status && b.status.startsWith("CONFIRMED") ? "text-green-700" : b.status && b.status.startsWith("FAILED") ? "text-red-600" : "text-gray-700"}`}>
                    {b.status}
                  </div>
                  <div className="text-xs text-gray-500">Seats: {b.totalBookings}</div>

                  <div className="flex gap-2 mt-2">
                    <button
                      onClick={() => navigate(`/events/${b.eventId}`)}
                      className="px-3 py-1 text-xs rounded border border-gray-200"
                    >
                      View event
                    </button>

                    {/* If booking confirmed, we show a non-clickable badge */}
                    {b.status && b.status.startsWith("CONFIRMED") ? (
                      <div className="px-3 py-1 text-xs rounded bg-green-100 text-green-800">Booked</div>
                    ) : null}
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}

        <div className="mt-6">
          <button onClick={fetchBookings} className="px-4 py-2 rounded bg-purple-600 text-white">
            Refresh
          </button>
        </div>
      </div>

      <ToastContainer position="top-center" autoClose={2000} />
    </div>
  );
}
