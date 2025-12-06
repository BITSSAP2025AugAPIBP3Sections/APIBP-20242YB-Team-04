// src/pages/Dashboard.jsx
import React, { useEffect, useRef, useState, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { FiChevronLeft, FiChevronRight } from "react-icons/fi";
import { searchApi } from "../api/api";
import CategoryCard from "../componenets/CategoryCard";
import EventCard from "../componenets/EventCard";
import { useSelector } from "react-redux";

/**
 * Category icons mapping (you can add/change icons)
 * using react-icons for simple visuals.
 */
import {
  FaHandsHelping,
  FaLeaf,
  FaHeartbeat,
  FaUniversity,
  FaLaptopCode,
  FaBolt,
  FaHandHoldingHeart,
} from "react-icons/fa";

const CATEGORY_ICON_MAP = {
  "Social Welfare": FaHandsHelping,
  "Animal Welfare": FaHandHoldingHeart,
  Health: FaHeartbeat,
  Education: FaUniversity,
  Technology: FaLaptopCode,
  Environment: FaLeaf,
  "Disaster Relief": FaBolt,
};

export default function Dashboard() {
  const navigate = useNavigate();

  // categories
  const [categories, setCategories] = useState([]);
  const [catLoading, setCatLoading] = useState(false);
  const [catError, setCatError] = useState(null);

  // suggested events
  const [suggested, setSuggested] = useState([]);
  const [suggestedLoading, setSuggestedLoading] = useState(false);
  const [suggestedError, setSuggestedError] = useState(null);

  // slider ref
  const catScrollerRef = useRef(null);

  // active category for UI highlight (optional)
  const [activeCategory, setActiveCategory] = useState(null);

  const selectedCity = useSelector((state) => state.city.city);

  // fetch categories once
  useEffect(() => {
    const fetchFilters = async () => {
      setCatLoading(true);
      setCatError(null);
      try {
        const res = await searchApi.get("/search/filters");
        const cats = Array.isArray(res.data?.categories)
          ? res.data.categories
          : [];
        const sorted = [...cats].sort((a, b) => a.localeCompare(b));
        setCategories(sorted);
      } catch (err) {
        console.error("Failed to fetch categories:", err);
        setCatError("Unable to load categories");
        setCategories([]);
      } finally {
        setCatLoading(false);
      }
    };

    fetchFilters();
  }, []);

  // fetch suggested events using searchApi and city from Redux/localStorage
  const fetchSuggested = useCallback(async () => {
    setSuggestedLoading(true);
    setSuggestedError(null);
    try {
      const cityFromRedux = selectedCity || "";
      const cityFromStorage = localStorage.getItem("eventix_city") || "";
      const city = cityFromRedux || cityFromStorage;

      const params = {
        page: 0,
        limit: 10,
        sortBy: "popularity",
      };

      if (city) params.city = city;

      const res = await searchApi.get("/search/events", { params });

      let list = [];
      if (Array.isArray(res.data?.content)) list = res.data.content;
      else if (Array.isArray(res.data)) list = res.data;
      else if (Array.isArray(res.data?.events)) list = res.data.events;

      setSuggested(list);
    } catch (err) {
      console.error("Failed to fetch suggested events:", err);
      setSuggestedError("Unable to load suggested events");
      setSuggested([]);
    } finally {
      setSuggestedLoading(false);
    }
  }, [selectedCity]);

  useEffect(() => {
    fetchSuggested();
  }, [fetchSuggested]);

  const onCategoryClick = (category) => {
    setActiveCategory(category);
    navigate(`/events?category=${encodeURIComponent(category)}`);
  };

  const onEventClick = (event) => {
    navigate(`/events/${event.id}`);
  };

  // slider controls: scroll ~70% of visible width
  const scrollBy = (dir = "right") => {
    const el = catScrollerRef.current;
    if (!el) return;
    const scrollAmount = Math.round(el.clientWidth * 0.7);
    const newPos =
      dir === "right"
        ? el.scrollLeft + scrollAmount
        : el.scrollLeft - scrollAmount;
    el.scrollTo({ left: newPos, behavior: "smooth" });
  };

  return (
    <div className="min-h-screen py-8 px-4">
      <div className="container mx-auto max-w-6xl">
        {/* Categories section */}
        <section className="mb-10">
          <div className="mb-4 flex items-center justify-between">
            <h2 className="text-xl font-semibold text-white">Categories</h2>

            <div className="hidden items-center gap-2 md:flex">
              <button
                onClick={() => scrollBy("left")}
                aria-label="Scroll categories left"
                className="flex h-9 w-9 items-center justify-center rounded-full border border-white/20 bg-black/30 text-gray-100 shadow-sm hover:bg-white/10 focus:outline-none"
              >
                <FiChevronLeft className="text-gray-200" size={18} />
              </button>
              <button
                onClick={() => scrollBy("right")}
                aria-label="Scroll categories right"
                className="flex h-9 w-9 items-center justify-center rounded-full border border-white/20 bg-black/30 text-gray-100 shadow-sm hover:bg-white/10 focus:outline-none"
              >
                <FiChevronRight className="text-gray-200" size={18} />
              </button>
            </div>
          </div>

          {catLoading ? (
            <div className="text-sm text-gray-300">Loading categories...</div>
          ) : catError ? (
            <div className="text-sm text-red-300">{catError}</div>
          ) : (
            <div
              ref={catScrollerRef}
              className="scrollbar-hide -mx-1 flex gap-4 overflow-x-auto px-1 py-2"
              role="list"
            >
              {categories.map((c) => {
                const Icon = CATEGORY_ICON_MAP[c] || null;
                return (
                  <div key={c} className="flex-shrink-0" role="listitem">
                    <div
                      className={`transition-transform ${
                        activeCategory === c ? "scale-105" : ""
                      }`}
                    >
                      <CategoryCard
                        label={c}
                        Icon={Icon}
                        onClick={onCategoryClick}
                      />
                    </div>
                  </div>
                );
              })}
            </div>
          )}
        </section>

        {/* Suggested events */}
        <section>
          <div className="mb-4 flex items-center justify-between">
            <h2 className="text-xl font-semibold text-white">
              Suggested events
            </h2>
            <button
              onClick={() => navigate("/events")}
              className="text-sm font-semibold text-purple-300 hover:text-purple-200 hover:underline"
            >
              See all
            </button>
          </div>

          {suggestedLoading ? (
            <div className="text-sm text-gray-300">
              Loading suggested events...
            </div>
          ) : suggestedError ? (
            <div className="text-sm text-red-300">{suggestedError}</div>
          ) : suggested.length === 0 ? (
            <div className="rounded-2xl border border-white/10 bg-white/5 p-4 text-sm text-gray-200 backdrop-blur">
              No suggested events yet. Try changing your city or search
              criteria.
            </div>
          ) : (
            <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
              {suggested.map((ev) => (
                <EventCard key={ev.id} event={ev} onClick={onEventClick} />
              ))}
            </div>
          )}
        </section>
      </div>
    </div>
  );
}
