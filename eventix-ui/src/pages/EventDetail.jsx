// src/pages/EventDetail.jsx
import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { eventsApi } from "../api/api"; // GET event details
import { useSelector } from "react-redux";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { FiLink } from "react-icons/fi";

/** small UUID v4 */
function uuidv4() {
  return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
    const r = (Math.random() * 16) | 0;
    const v = c === "x" ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
}

const buildBookingPayload = ({ bookingId, event, user, seats }) => ({
  id: bookingId,
  eventId: event.id,
  userId: user?.userId?.toString?.() || user?.sub || "anonymous",
  totalBookings: Number(seats),
  recentBookings: Number(seats),
  wishlistCount: 0,
  avgRating: 0,
  timestamp: new Date().toISOString(),
  city: event.city || "",
  category: event.category || "",
  status: "REQUESTED",
  wishlisted: false,
});

export default function EventDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const user = useSelector((st) => st.userAuth?.token || null);
  const userId = user?.userId?.toString?.() || null;

  const [event, setEvent] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // booking state
  const [bookingOpen, setBookingOpen] = useState(false);
  const [bookingLoading, setBookingLoading] = useState(false);
  const [name, setName] = useState(
    user ? `${user.firstName || ""} ${user.lastName || ""}`.trim() : ""
  );
  const [email, setEmail] = useState(user?.sub || "");
  const [seatsToBook, setSeatsToBook] = useState(1);

  // booking check
  const [isBooked, setIsBooked] = useState(false);
  const [userBookings, setUserBookings] = useState([]);
  const [checkingBookings, setCheckingBookings] = useState(false);

  // fetch event details
  useEffect(() => {
    let mounted = true;
    const fetchEvent = async () => {
      setLoading(true);
      setError(null);
      try {
        const res = await eventsApi.get(`/events/${id}`);
        if (!mounted) return;
        setEvent(res.data);
      } catch (err) {
        console.error("Fetch event error:", err);
        if (!mounted) return;
        setError("Unable to load event. Please try again.");
      } finally {
        if (mounted) setLoading(false);
      }
    };

    if (id) fetchEvent();
    return () => (mounted = false);
  }, [id]);

  // fetch user's bookings and check if they booked this event
  useEffect(() => {
    let mounted = true;
    const fetchBookings = async () => {
      if (!userId) {
        setUserBookings([]);
        setIsBooked(false);
        return;
      }
      setCheckingBookings(true);
      try {
        const accessToken =
          localStorage.getItem("accessToken") || localStorage.getItem("token");
        const headers = accessToken ? { Authorization: `Bearer ${accessToken}` } : {};
        const url = `http://localhost:8082/bookings?userId=${encodeURIComponent(
          userId
        )}`;
        const res = await axios.get(url, { headers, timeout: 8000 });
        const list = Array.isArray(res.data) ? res.data : [];
        if (!mounted) return;
        setUserBookings(list);
        const hasBooking = list.some(
          (b) =>
            String(b.eventId) === String(id) &&
            b.status &&
            b.status !== "FAILED_EVENT_SERVICE_DOWN"
        );
        setIsBooked(Boolean(hasBooking));
      } catch (err) {
        console.error("Failed to fetch user bookings:", err);
        if (!mounted) return;
        // don't surface as fatal error; just assume not booked
        setUserBookings([]);
        setIsBooked(false);
      } finally {
        if (mounted) setCheckingBookings(false);
      }
    };

    fetchBookings();
    return () => (mounted = false);
  }, [userId, id]);

  // keep name/email synced if user logs in later
  useEffect(() => {
    if (user) {
      setName(`${user.firstName || ""} ${user.lastName || ""}`.trim());
      setEmail(user.sub || "");
    }
  }, [user]);

  const formatDateTime = (iso) => {
    try {
      const d = new Date(iso);
      return d.toLocaleString(undefined, {
        day: "numeric",
        month: "short",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      });
    } catch {
      return iso;
    }
  };

  const openBooking = () => {
    if (!user) {
      toast.info("Please login to book seats.");
      navigate("/login");
      return;
    }
    setBookingOpen(true);
  };

  // Submit booking to booking service
  const submitBooking = async (e) => {
    e.preventDefault();
    if (!seatsToBook || seatsToBook < 1) {
      toast.error("Choose at least 1 seat.");
      return;
    }
    if (!event) return;
    if (seatsToBook > event.seatsAvailable) {
      toast.error("Not enough seats available.");
      return;
    }

    setBookingLoading(true);

    try {
      const bookingId = uuidv4();
      const payload = buildBookingPayload({
        bookingId,
        event,
        user,
        seats: seatsToBook,
      });

      const bookingUrl = "http://localhost:8082/bookings";
      const accessToken =
        localStorage.getItem("accessToken") || localStorage.getItem("token");
      const headers = accessToken ? { Authorization: `Bearer ${accessToken}` } : {};

      const res = await axios.post(bookingUrl, payload, {
        headers,
        timeout: 10000,
      });

      const bookingResp = res.data || {};
      const respStatus = bookingResp.status || "";

      if (respStatus && respStatus.startsWith("FAILED")) {
        toast.error(`Booking failed: ${respStatus}`);
      } else {
        toast.success("Booking request submitted.");
        // optimistic update
        setEvent((prev) =>
          prev
            ? { ...prev, seatsAvailable: prev.seatsAvailable - seatsToBook }
            : prev
        );
        setBookingOpen(false);
        // refetch bookings so UI updates to show "Booked"
        try {
          const url = `http://localhost:8082/bookings?userId=${encodeURIComponent(
            userId
          )}`;
          const accessToken2 =
            localStorage.getItem("accessToken") || localStorage.getItem("token");
          const headers2 = accessToken2
            ? { Authorization: `Bearer ${accessToken2}` }
            : {};
          const result = await axios.get(url, {
            headers: headers2,
            timeout: 8000,
          });
          const list = Array.isArray(result.data) ? result.data : [];
          setUserBookings(list);
          const hasBooking = list.some(
            (b) =>
              String(b.eventId) === String(id) &&
              b.status &&
              b.status !== "FAILED_EVENT_SERVICE_DOWN"
          );
          setIsBooked(Boolean(hasBooking));
        } catch (err) {
          console.warn("Refetch bookings failed after booking:", err);
        }
      }
    } catch (err) {
      console.error("Booking error:", err);
      const status = err.response?.status;
      if (status === 401) {
        toast.error("Please login to continue.");
        navigate("/login");
        return;
      }
      const msg =
        err.response?.data?.message ||
        err.response?.data?.status ||
        "Booking failed. Try again.";
      toast.error(msg);
    } finally {
      setBookingLoading(false);
    }
  };

  const copyLink = async () => {
    try {
      const url = `${window.location.origin}/events/${id}`;
      await navigator.clipboard.writeText(url);
      toast.success("Event link copied to clipboard");
    } catch {
      toast.error("Unable to copy link");
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="text-sm text-gray-200">Loading event...</div>
      </div>
    );
  }

  if (error || !event) {
    return (
      <div className="min-h-screen flex items-center justify-center px-4">
        <div className="max-w-xl rounded-2xl border border-white/10 bg-white/5 p-6 text-center text-sm text-gray-100 backdrop-blur">
          <p className="mb-4 text-red-300">{error || "Event not found"}</p>
          <div className="flex justify-center gap-3">
            <button
              onClick={() => navigate(-1)}
              className="rounded-full bg-purple-500 px-4 py-2 text-sm font-semibold text-white hover:bg-purple-400"
            >
              Go back
            </button>
            <button
              onClick={() => navigate("/")}
              className="rounded-full border border-white/30 px-4 py-2 text-sm font-semibold text-gray-100 hover:bg-white/10"
            >
              Home
            </button>
          </div>
        </div>
        <ToastContainer position="top-center" autoClose={2500} />
      </div>
    );
  }

  // UI for Book / Booked
  const BookButton = () => {
    if (checkingBookings) {
      return (
        <button
          className="rounded-full bg-white/10 px-4 py-2 text-sm text-gray-200"
          disabled
        >
          Checking...
        </button>
      );
    }

    if (isBooked) {
      return (
        <button
          className="flex items-center gap-2 rounded-full bg-green-500 px-4 py-2 text-sm font-semibold text-white shadow-lg shadow-green-500/40"
          disabled
        >
          <svg
            className="h-4 w-4"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
          >
            <path
              d="M20 6L9 17l-5-5"
              strokeWidth="2"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </svg>
          Booked
        </button>
      );
    }

    return (
      <button
        onClick={openBooking}
        disabled={event.seatsAvailable <= 0}
        className={`rounded-full px-5 py-2 text-sm font-semibold text-white shadow-md ${
          event.seatsAvailable > 0
            ? "bg-purple-500 hover:bg-purple-400 shadow-purple-500/40"
            : "bg-gray-600 cursor-not-allowed"
        }`}
      >
        {event.seatsAvailable > 0 ? "Book" : "Sold out"}
      </button>
    );
  };

  return (
    <div className="min-h-screen py-10 px-4">
      <div className="container mx-auto max-w-4xl">
        <div className="rounded-3xl border border-white/10 bg-white/5 p-6 md:p-8 shadow-[0_18px_45px_rgba(0,0,0,0.6)] backdrop-blur">
          {/* Header */}
          <div className="flex items-start justify-between gap-4">
            <div className="flex-1">
              <h1 className="text-2xl font-bold text-white">{event.title}</h1>
              <p className="mt-1 text-sm text-gray-300">
                {event.category} • {event.city} • {event.venue}
              </p>
              <p className="mt-1 text-xs text-gray-400">
                Organized by: {event.organizerId}
              </p>
            </div>

            <div className="flex flex-col items-end gap-2 text-right">
              <div className="text-xs text-gray-400">Status</div>
              <div className="text-xs font-semibold text-green-300">
                {event.status}
              </div>
              <button
                onClick={copyLink}
                title="Copy event link"
                className="mt-1 rounded-full border border-white/20 p-2 text-gray-200 hover:bg-white/10"
              >
                <FiLink />
              </button>
            </div>
          </div>

          {/* Meta */}
          <div className="mt-6 grid grid-cols-1 gap-4 text-sm text-gray-200 md:grid-cols-3">
            <div>
              <div className="text-xs uppercase tracking-[0.16em] text-gray-400">
                When
              </div>
              <div className="mt-1 font-medium">
                {formatDateTime(event.startTime)}
              </div>
              <div className="text-xs text-gray-300">
                Ends: {formatDateTime(event.endTime)}
              </div>
            </div>

            <div>
              <div className="text-xs uppercase tracking-[0.16em] text-gray-400">
                Capacity
              </div>
              <div className="mt-1 font-medium">
                {event.capacity}{" "}
                <span className="text-xs font-normal text-gray-300">
                  seats
                </span>
              </div>
              <div className="mt-1 text-xs text-gray-300">
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
            </div>

            <div>
              <div className="text-xs uppercase tracking-[0.16em] text-gray-400">
                Venue
              </div>
              <div className="mt-1 font-medium text-gray-100">
                {event.venue}
              </div>
              <a
                href={`https://www.google.com/maps/search/?api=1&query=${event.latitude},${event.longitude}`}
                target="_blank"
                rel="noreferrer"
                className="mt-1 inline-block text-xs font-medium text-purple-300 hover:text-purple-200 hover:underline"
              >
                Open in maps
              </a>
            </div>
          </div>

          {/* Description */}
          <div className="mt-6">
            <h3 className="text-sm font-semibold uppercase tracking-[0.16em] text-gray-300">
              About
            </h3>
            <p className="mt-2 text-sm leading-relaxed text-gray-100">
              {event.description}
            </p>
          </div>

          {/* Actions */}
          <div className="mt-6 flex flex-wrap items-center gap-3">
            <BookButton />
            <button
              onClick={() => navigate(-1)}
              className="rounded-full border border-white/25 px-4 py-2 text-sm text-gray-100 hover:bg-white/10"
            >
              Back
            </button>
          </div>

          {/* User booking info */}
          {isBooked && userBookings.length > 0 && (
            <div className="mt-5 rounded-2xl border border-green-400/30 bg-green-500/10 p-4 text-xs text-green-50">
              <div className="text-sm font-semibold text-green-100">
                You have a booking for this event
              </div>
              <div className="mt-2 space-y-2">
                {userBookings
                  .filter((b) => String(b.eventId) === String(id))
                  .map((b) => (
                    <div key={b.id}>
                      <div>
                        Booking ID:{" "}
                        <span className="font-semibold">{b.id}</span>
                      </div>
                      <div>
                        Status:{" "}
                        <span className="font-semibold">{b.status}</span>
                      </div>
                      <div>Seats: {b.totalBookings}</div>
                      <div className="text-[11px] text-green-100/80">
                        Booked: {new Date(b.timestamp).toLocaleString()}
                      </div>
                    </div>
                  ))}
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Booking Modal */}
      {bookingOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 p-4">
          <div className="w-full max-w-md rounded-2xl border border-white/10 bg-[#090816] p-6 text-sm text-gray-100 shadow-[0_24px_55px_rgba(0,0,0,0.9)] backdrop-blur">
            <h3 className="text-lg font-semibold text-white">Book seats</h3>

            <form className="mt-4 space-y-4" onSubmit={submitBooking}>
              <div>
                <label className="block text-xs font-medium text-gray-300">
                  Name
                </label>
                <input
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
                  className="mt-1 w-full rounded-xl border border-white/20 bg-black/30 px-3 py-2 text-sm text-white outline-none placeholder:text-gray-500 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                  placeholder="Your name"
                />
              </div>

              <div>
                <label className="block text-xs font-medium text-gray-300">
                  Email
                </label>
                <input
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  type="email"
                  className="mt-1 w-full rounded-xl border border-white/20 bg-black/30 px-3 py-2 text-sm text-white outline-none placeholder:text-gray-500 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                  placeholder="you@example.com"
                />
              </div>

              <div>
                <label className="block text-xs font-medium text-gray-300">
                  Seats
                </label>
                <input
                  value={seatsToBook}
                  onChange={(e) => {
                    const v = Number(e.target.value) || 1;
                    setSeatsToBook(
                      Math.max(1, Math.min(v, event.seatsAvailable))
                    );
                  }}
                  min={1}
                  max={event.seatsAvailable}
                  type="number"
                  className="mt-1 w-28 rounded-xl border border-white/20 bg-black/30 px-3 py-2 text-sm text-white outline-none focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                />
                <div className="mt-1 text-[11px] text-gray-400">
                  {event.seatsAvailable} seats available
                </div>
              </div>

              <div className="mt-3 flex items-center justify-between">
                <button
                  type="submit"
                  disabled={bookingLoading}
                  className={`rounded-full px-4 py-2 text-sm font-semibold text-white ${
                    bookingLoading
                      ? "bg-gray-600 cursor-not-allowed"
                      : "bg-purple-500 hover:bg-purple-400 shadow-md shadow-purple-500/40"
                  }`}
                >
                  {bookingLoading ? "Booking..." : "Confirm booking"}
                </button>

                <button
                  type="button"
                  onClick={() => setBookingOpen(false)}
                  className="rounded-full border border-white/25 px-4 py-2 text-sm text-gray-100 hover:bg-white/10"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      <ToastContainer position="top-center" autoClose={2500} />
    </div>
  );
}
