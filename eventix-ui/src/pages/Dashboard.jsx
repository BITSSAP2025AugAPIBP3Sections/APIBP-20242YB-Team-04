// src/pages/Dashboard.jsx
import React, { useEffect, useRef, useState } from "react";
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

  useEffect(() => {
    // fetch categories (filters endpoint)
    const fetchFilters = async () => {
      setCatLoading(true);
      setCatError(null);
      try {
        const res = await searchApi.get("/search/filters");
        const cats = Array.isArray(res.data?.categories)
          ? res.data.categories
          : [];
        // sort alphabetically for predictability
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

    // fetch suggested events
    // const fetchSuggested = async () => {
    //   setSuggestedLoading(true);
    //   setSuggestedError(null);
    //   try {
    //     // Hits: http://localhost:8083/api/v1/events?page=0&size=10
    //     const res = await eventsApi.get("/events", { params: { page: 0, size: 10 } });
    //     const list = Array.isArray(res.data?.content) ? res.data.content : res.data?.content || [];
    //     setSuggested(list);
    //   } catch (err) {
    //     console.error("Failed to fetch suggested events:", err);
    //     setSuggestedError("Unable to load suggested events");
    //     setSuggested([]);
    //   } finally {
    //     setSuggestedLoading(false);
    //   }
    // };

        fetchFilters();
    // fetchSuggested();
    }, []);

    // fetch suggested events using searchApi and city from localStorage
    const fetchSuggested = async () => {
      setSuggestedLoading(true);
      setSuggestedError(null);
      try {
        const cityFromStorage = localStorage.getItem("eventix_city") || "";

        const params = {
          page: 0,
          limit: 10,
          sortBy: "popularity",
        };

        // only include city when available
        if (cityFromStorage) params.city = cityFromStorage;

        // Use search/events API
        const res = await searchApi.get("/search/events", { params });

        // defensive parsing for multiple backend response formats
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
    };

    useEffect(() => {
      fetchSuggested(selectedCity);
    }, [selectedCity]);



  const onCategoryClick = (category) => {
    setActiveCategory(category);
    // navigate to events page filtered by category
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
    <div className="min-h-screen bg-gray-50 py-8 px-4">
      <div className="container mx-auto">
        {/* Categories section */}
        <section className="mb-8">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-semibold text-gray-900">Categories</h2>

            <div className="hidden md:flex items-center gap-2">
              <button
                onClick={() => scrollBy("left")}
                aria-label="Scroll categories left"
                className="p-2 rounded-full bg-white border border-gray-200 shadow-sm hover:shadow-md focus:outline-none"
              >
                <FiChevronLeft className="text-gray-600" size={18} />
              </button>
              <button
                onClick={() => scrollBy("right")}
                aria-label="Scroll categories right"
                className="p-2 rounded-full bg-white border border-gray-200 shadow-sm hover:shadow-md focus:outline-none"
              >
                <FiChevronRight className="text-gray-600" size={18} />
              </button>
            </div>
          </div>

          {catLoading ? (
            <div className="text-sm text-gray-500">Loading categories...</div>
          ) : catError ? (
            <div className="text-sm text-red-500">{catError}</div>
          ) : (
            <div
              ref={catScrollerRef}
              className="flex gap-4 overflow-x-auto py-2 px-1 -mx-1 scrollbar-hide"
              // .scrollbar-hide helper can be added to global CSS if you want to hide browser scrollbar
              role="list"
            >
              {categories.map((c) => {
                const Icon = CATEGORY_ICON_MAP[c] || null;
                return (
                  <div key={c} className="flex-shrink-0" role="listitem">
                    <div
                      className={`transition-transform ${
                        activeCategory === c ? "transform scale-105" : ""
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
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-semibold text-gray-900">
              Suggested events
            </h2>
            <button
              onClick={() => navigate("/events")}
              className="text-sm text-purple-600 font-semibold hover:underline"
            >
              See all
            </button>
          </div>

          {suggestedLoading ? (
            <div className="text-sm text-gray-500">
              Loading suggested events...
            </div>
          ) : suggestedError ? (
            <div className="text-sm text-red-500">{suggestedError}</div>
          ) : suggested.length === 0 ? (
            <div className="text-sm text-gray-500">No suggested events yet</div>
          ) : (
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
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
