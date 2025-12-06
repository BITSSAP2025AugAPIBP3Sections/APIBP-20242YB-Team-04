// src/components/Navbar.jsx
import { useState, useEffect, useRef, useCallback } from "react";
import axios from "axios";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { FiSearch, FiMapPin, FiUser, FiX } from "react-icons/fi";
import { clearUser } from "../redux/reducers/userAuthReducer";
import { setCity } from "../redux/reducers/citySlice";
import { searchApi } from "../api/api"; // axios instance for search service

export default function Navbar() {
  const menu = [
    { name: "For you", active: true },
    { name: "Dining" },
    { name: "Events" },
    { name: "Movies" },
    { name: "Activities" },
    { name: "Play" },
    { name: "Stores" },
  ];

  const dispatch = useDispatch();
  const navigate = useNavigate();

  // --- Cities & City dropdown state ---
  const [cities, setCities] = useState([]);
  const [loadingCities, setLoadingCities] = useState(false);
  const [cityError, setCityError] = useState(null);
  const [openCity, setOpenCity] = useState(false);

  const [selectedCity, setSelectedCity] = useState(
    () => localStorage.getItem("eventix_city") || "Gurugram"
  );

  const cityDropdownRef = useRef(null);

  useEffect(() => {
    const fetchFilters = async () => {
      setLoadingCities(true);
      setCityError(null);
      try {
        const res = await axios.get(
          "http://localhost:8084/api/v1/search/filters"
        );
        const cityList = Array.isArray(res.data?.cities) ? res.data.cities : [];

        // Sort alphabetically before storing
        const sortedCities = [...cityList].sort((a, b) => a.localeCompare(b));
        setCities(sortedCities);
      } catch (err) {
        console.error("Failed to fetch filters:", err);
        setCityError("Unable to load cities");
        setCities([]);
      } finally {
        setLoadingCities(false);
      }
    };

    fetchFilters();
  }, []);

  const chooseCity = (c) => {
    setSelectedCity(c);
    localStorage.setItem("eventix_city", c);
    dispatch(setCity(c));
    // also notify same-tab listeners
    window.dispatchEvent(new CustomEvent("eventix_city_change", { detail: c }));
    setOpenCity(false);
  };

  // --- User / Profile state ---
  // expects decoded payload stored in state.userAuth.token
  const user = useSelector((state) => state.userAuth?.token || null);

  const getInitials = (u) => {
    if (!u) return null;
    const fn = (u.firstName || "").trim();
    const ln = (u.lastName || "").trim();
    const firstLetter = fn ? fn.charAt(0).toUpperCase() : "";
    const lastLetter = ln ? ln.charAt(0).toUpperCase() : "";
    const initials = `${firstLetter}${lastLetter}`.trim();
    return initials || null;
  };

  const initials = getInitials(user);

  const [openProfile, setOpenProfile] = useState(false);
  const profileRef = useRef(null);

  const handleLogout = () => {
    dispatch(clearUser());
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    // keep city if you want
    setOpenProfile(false);
    navigate("/login");
  };

  // ----------------- Search with suggestions -----------------
  const [query, setQuery] = useState("");
  const [searchOpen, setSearchOpen] = useState(false); // for mobile expand
  const inputRef = useRef(null);

  // suggestions
  const [suggestions, setSuggestions] = useState([]);
  const [suggestionsLoading, setSuggestionsLoading] = useState(false);
  const [suggestionsError, setSuggestionsError] = useState(null);

  // keyboard nav
  const [highlightIndex, setHighlightIndex] = useState(-1);

  // debounce + abort controller refs
  const debounceRef = useRef(null);
  const activeControllerRef = useRef(null);
  const lastQueryRef = useRef("");

  // keep selectedCity synced from localStorage changes in other tabs OR from setCity dispatch
  useEffect(() => {
    const onStorage = (e) => {
      if (e.key === "eventix_city") {
        setSelectedCity(e.newValue || "");
      }
    };
    // also listen to custom event dispatched on chooseCity for same-tab updates
    const onCustom = (e) => {
      const c = e?.detail;
      if (c) setSelectedCity(c);
    };
    window.addEventListener("storage", onStorage);
    window.addEventListener("eventix_city_change", onCustom);
    return () => {
      window.removeEventListener("storage", onStorage);
      window.removeEventListener("eventix_city_change", onCustom);
    };
  }, []);

  useEffect(() => {
    if (searchOpen && inputRef.current) inputRef.current.focus();
  }, [searchOpen]);

  // fetch suggestions (debounced)
  const fetchSuggestions = useCallback(
    async (q) => {
      const trimmed = (q || "").trim();
      if (!trimmed) {
        setSuggestions([]);
        setSuggestionsLoading(false);
        setSuggestionsError(null);
        return;
      }

      // cancel previous request
      if (activeControllerRef.current) {
        try {
          activeControllerRef.current.abort();
        } catch {}
        activeControllerRef.current = null;
      }

      setSuggestionsLoading(true);
      setSuggestionsError(null);
      lastQueryRef.current = trimmed;

      try {
        const controller = new AbortController();
        activeControllerRef.current = controller;

        const params = {
          q: trimmed,
          page: 0,
          limit: 5,
          sortBy: "popularity",
        };
        if (selectedCity) params.city = selectedCity;

        const res = await searchApi.get("/search/events", {
          params,
          signal: controller.signal,
          timeout: 8000,
        });

        // ignore stale responses
        if (lastQueryRef.current !== trimmed) return;

        let list = [];
        if (Array.isArray(res.data?.content)) list = res.data.content;
        else if (Array.isArray(res.data)) list = res.data;
        else if (Array.isArray(res.data?.events)) list = res.data.events;
        else list = [];

        setSuggestions(list);
      } catch (err) {
        if (err?.name === "CanceledError" || err?.message === "canceled") {
          // ignore cancellation
          return;
        }
        console.error("Search suggestions error:", err);
        setSuggestionsError("Unable to load suggestions");
        setSuggestions([]);
      } finally {
        setSuggestionsLoading(false);
        activeControllerRef.current = null;
      }
    },
    [selectedCity]
  );

  // debounce effect
  useEffect(() => {
    if (debounceRef.current) clearTimeout(debounceRef.current);

    setHighlightIndex(-1);

    const trimmed = (query || "").trim();
    if (!trimmed) {
      // cancel any active request
      if (activeControllerRef.current) {
        try {
          activeControllerRef.current.abort();
        } catch {}
        activeControllerRef.current = null;
      }
      setSuggestions([]);
      setSuggestionsLoading(false);
      setSuggestionsError(null);
      return;
    }

    debounceRef.current = setTimeout(() => {
      fetchSuggestions(query);
    }, 300);

    return () => {
      if (debounceRef.current) clearTimeout(debounceRef.current);
    };
  }, [query, fetchSuggestions]);

  // navigate to event detail from suggestion
  const goToEvent = (ev) => {
    setSuggestions([]);
    setQuery("");
    setSearchOpen(false);
    navigate(`/events/${ev.id}`);
  };

  // full search navigation (Enter)
  const doSearchFull = (q) => {
    const trimmed = (q || "").trim();
    if (!trimmed) return;
    const params = new URLSearchParams();
    params.set("q", trimmed);
    if (selectedCity) params.set("city", selectedCity);
    params.set("sortBy", "popularity");
    params.set("page", "0");
    params.set("limit", "10");

    setSuggestions([]);
    setSearchOpen(false);
    navigate(`/events?${params.toString()}`);
  };

  // keyboard interaction on input
  const onInputKeyDown = (e) => {
    if (e.key === "ArrowDown") {
      e.preventDefault();
      setHighlightIndex((i) => Math.min(i + 1, suggestions.length - 1));
      return;
    }
    if (e.key === "ArrowUp") {
      e.preventDefault();
      setHighlightIndex((i) => Math.max(i - 1, 0));
      return;
    }
    if (e.key === "Enter") {
      if (highlightIndex >= 0 && highlightIndex < suggestions.length) {
        e.preventDefault();
        goToEvent(suggestions[highlightIndex]);
      } else {
        doSearchFull(query);
      }
    }
    if (e.key === "Escape") {
      setSuggestions([]);
      setHighlightIndex(-1);
      setSearchOpen(false);
    }
  };

  // click outside suggestions closes them
  useEffect(() => {
    const onDocClick = (e) => {
      const el = document.getElementById("eventix-search-container");
      if (el && !el.contains(e.target)) {
        setSuggestions([]);
        setHighlightIndex(-1);
      }
    };
    document.addEventListener("click", onDocClick);
    return () => document.removeEventListener("click", onDocClick);
  }, []);

  // Close dropdowns/popovers on outside click or Escape
  useEffect(() => {
    const handleDocClick = (e) => {
      if (
        cityDropdownRef.current &&
        !cityDropdownRef.current.contains(e.target)
      ) {
        setOpenCity(false);
      }

      if (profileRef.current && !profileRef.current.contains(e.target)) {
        setOpenProfile(false);
      }
    };

    const handleEsc = (e) => {
      if (e.key === "Escape" || e.key === "Esc") {
        setOpenCity(false);
        setOpenProfile(false);
        setSearchOpen(false);
        setSuggestions([]);
      }
    };

    document.addEventListener("click", handleDocClick);
    document.addEventListener("keydown", handleEsc);
    return () => {
      document.removeEventListener("click", handleDocClick);
      document.removeEventListener("keydown", handleEsc);
    };
  }, []);

  return (
    <nav className="w-full border-b border-white/10 bg-black/40 backdrop-blur-md relative z-50">
      <div className="max-w-8xl mx-auto px-6 py-3 flex items-center justify-between">
        {/* LEFT SECTION */}
        <div className="flex items-center gap-6">
          <h1
            className="text-2xl font-bold text-white cursor-pointer"
            onClick={() => navigate("/")}
          >
            Eventix
          </h1>

          <div className="h-6 w-[1px] bg-white/20" />

          {/* City selector */}
          <div className="relative" ref={cityDropdownRef}>
            <button
              type="button"
              onClick={() => setOpenCity((s) => !s)}
              className="flex items-center gap-2 text-gray-100 font-semibold px-3 py-1 rounded hover:bg-white/10 focus:outline-none"
              aria-expanded={openCity}
              aria-haspopup="listbox"
            >
              <FiMapPin className="text-purple-300" size={16} />
              <div className="text-left">
                <div className="text-sm">{selectedCity}</div>
                <p className="text-xs text-gray-400 -mt-1">Region</p>
              </div>
            </button>

            {openCity && (
              <div className="absolute z-40 mt-2 w-56 bg-[#050313] border border-white/15 rounded shadow-[0_18px_35px_rgba(0,0,0,0.8)]">
                <div className="p-3 text-xs text-gray-400 uppercase tracking-[0.16em]">
                  Select city
                </div>

                <div className="max-h-60 overflow-auto">
                  {loadingCities && (
                    <div className="px-3 py-2 text-sm text-gray-300">
                      Loading cities...
                    </div>
                  )}

                  {!loadingCities && cityError && (
                    <div className="px-3 py-2 text-sm text-red-300">
                      {cityError}
                    </div>
                  )}

                  {!loadingCities && !cityError && cities.length === 0 && (
                    <div className="px-3 py-2 text-sm text-gray-300">
                      No cities available
                    </div>
                  )}

                  {!loadingCities &&
                    !cityError &&
                    cities.map((c) => (
                      <button
                        key={c}
                        onClick={() => chooseCity(c)}
                        className={`w-full text-left px-3 py-2 text-sm rounded-none ${
                          c === selectedCity
                            ? "bg-purple-500/20 text-purple-100 font-semibold"
                            : "text-gray-100 hover:bg-white/5"
                        }`}
                      >
                        {c}
                      </button>
                    ))}
                </div>
              </div>
            )}
          </div>
        </div>

        {/* RIGHT SECTION */}
        <div className="flex items-center gap-6">
          {/* SEARCH container */}
          <div id="eventix-search-container" className="relative">
            {/* Desktop / tablet */}
            <form
              onSubmit={(e) => {
                e.preventDefault();
                doSearchFull(query);
              }}
              className="hidden md:flex items-center gap-2"
            >
              <div className="relative flex items-center bg-black/40 border border-white/15 rounded-md px-3 py-1 w-64 md:w-96">
                <FiSearch className="text-gray-300 mr-2" size={18} />
                <input
                  ref={inputRef}
                  value={query}
                  onChange={(e) => setQuery(e.target.value)}
                  onKeyDown={onInputKeyDown}
                  type="search"
                  placeholder="Search events, e.g. sports, workshop..."
                  aria-label="Search events"
                  className="bg-transparent outline-none text-sm text-white w-full placeholder:text-gray-500"
                />
                {query && (
                  <button
                    type="button"
                    onClick={() => {
                      setQuery("");
                      setSuggestions([]);
                    }}
                    className="ml-2 p-1 rounded hover:bg-white/10"
                    aria-label="Clear search"
                  >
                    <FiX size={14} className="text-gray-300" />
                  </button>
                )}
              </div>

              <button
                type="submit"
                className="px-3 py-1 bg-purple-500 text-white rounded-md text-sm font-medium hover:bg-purple-400 shadow-sm shadow-purple-500/40"
              >
                Search
              </button>
            </form>

            {/* Mobile: icon -> expands to input */}
            <div className="sm:hidden">
              {!searchOpen ? (
                <button
                  onClick={() => setSearchOpen(true)}
                  className="p-2 rounded-md hover:bg-white/10"
                  aria-label="Open search"
                >
                  <FiSearch size={18} className="text-purple-200" />
                </button>
              ) : (
                <form
                  onSubmit={(e) => {
                    e.preventDefault();
                    doSearchFull(query);
                  }}
                  className="flex items-center gap-2"
                >
                  <div className="relative flex items-center bg-black/40 border border-white/15 rounded-md px-3 py-1 w-64">
                    <FiSearch className="text-gray-300 mr-2" size={18} />
                    <input
                      ref={inputRef}
                      value={query}
                      onChange={(e) => setQuery(e.target.value)}
                      onKeyDown={onInputKeyDown}
                      type="search"
                      placeholder="Search events..."
                      aria-label="Search events"
                      className="bg-transparent outline-none text-sm w-full text-white placeholder:text-gray-500"
                    />
                    <button
                      type="button"
                      onClick={() => {
                        setQuery("");
                        setSearchOpen(false);
                        setSuggestions([]);
                      }}
                      className="ml-2 p-1 rounded hover:bg-white/10"
                      aria-label="Close search"
                    >
                      <FiX size={16} className="text-gray-300" />
                    </button>
                  </div>
                </form>
              )}
            </div>

            {/* Suggestions dropdown */}
            {(suggestions.length > 0 ||
              suggestionsLoading ||
              suggestionsError) && (
              <div className="absolute z-50 mt-2 w-[24rem] sm:w-[36rem] bg-[#050313] border border-white/15 rounded-md shadow-[0_18px_45px_rgba(0,0,0,0.9)] overflow-hidden">
                <div className="max-h-64 overflow-auto">
                  {suggestionsLoading && (
                    <div className="p-3 text-sm text-gray-300">
                      Searching...
                    </div>
                  )}

                  {suggestionsError && (
                    <div className="p-3 text-sm text-red-300">
                      {suggestionsError}
                    </div>
                  )}

                  {!suggestionsLoading &&
                    suggestions.length === 0 &&
                    !suggestionsError && (
                      <div className="p-3 text-sm text-gray-300">
                        No suggestions
                      </div>
                    )}

                  {suggestions.map((s, idx) => {
                    const highlighted = idx === highlightIndex;
                    const title = s.title || s.name || "";
                    const sub = `${s.city || ""}${
                      s.startTime
                        ? ` • ${new Date(s.startTime).toLocaleString()}`
                        : ""
                    }`;

                    return (
                      <button
                        key={s.id || `${title}-${idx}`}
                        onMouseEnter={() => setHighlightIndex(idx)}
                        onMouseLeave={() => setHighlightIndex(-1)}
                        onClick={() => goToEvent(s)}
                        className={`w-full text-left px-4 py-3 flex items-start gap-3 ${
                          highlighted
                            ? "bg-white/10"
                            : "hover:bg-white/5"
                        }`}
                        role="option"
                        aria-selected={highlighted}
                      >
                        <div className="flex-1">
                          <div className="text-sm font-medium text-gray-50 line-clamp-1">
                            {title}
                          </div>
                          <div className="text-xs text-gray-400 mt-1">
                            {sub}
                          </div>
                        </div>
                        <div className="text-xs text-gray-400">View</div>
                      </button>
                    );
                  })}
                </div>

                <div className="border-t border-white/10 px-3 py-2 flex items-center justify-between">
                  <div className="text-xs text-gray-400">
                    Press Enter to view full results for{" "}
                    <span className="font-medium text-gray-200">
                      {query || "..."}
                    </span>
                  </div>
                  <div>
                    <button
                      onClick={() => doSearchFull(query)}
                      className="px-3 py-1 bg-purple-500 text-white rounded text-sm hover:bg-purple-400"
                    >
                      See all
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>

          {/* CENTER MENU */}
          <div className="hidden md:flex items-center gap-6 ml-10">
            <button
              className="px-3 py-1 text-sm font-semibold transition rounded-full bg-white/5 text-gray-100 hover:bg-white/10"
              onClick={() => navigate("/")}
            >
              Home
            </button>
            <button
              className="px-3 py-1 text-sm font-semibold transition rounded-full bg-white/5 text-gray-100 hover:bg-white/10"
              onClick={() => navigate("/my-bookings")}
            >
              My Bookings
            </button>
            <button
              className="px-3 py-1 text-sm font-semibold transition rounded-full bg.white/5 text-gray-100 hover:bg-white/10"
              onClick={() => navigate("/about")}
            >
              About
            </button>
            <button
              className="px-3 py-1 text-sm font-semibold transition rounded-full bg.white/5 text-gray-100 hover:bg-white/10"
              onClick={() => navigate("/contact")}
            >
              Contact
            </button>
          </div>

          {/* Login Button OR Profile */}
          {!user ? (
            <button
              onClick={() => navigate("/login")}
              className="px-4 py-1.5 text-sm font-semibold text-purple-100 border border-purple-400 rounded-full hover:bg-purple-500/20 transition"
            >
              Login
            </button>
          ) : (
            <div className="relative" ref={profileRef}>
              <button
                type="button"
                onClick={() => setOpenProfile((s) => !s)}
                aria-haspopup="true"
                aria-expanded={openProfile}
                className="flex items-center gap-2 focus:outline-none"
              >
                {initials ? (
                  <div className="h-9 w-9 rounded-full bg-purple-500 text-white flex items-center justify-center font-semibold shadow-sm shadow-purple-500/40">
                    {initials}
                  </div>
                ) : (
                  <div className="h-9 w-9 rounded-full bg-white/10 flex items-center justify-center">
                    <FiUser className="text-gray-100" size={18} />
                  </div>
                )}
              </button>

              {/* Profile Popover */}
              {openProfile && (
                <div className="absolute right-0 mt-2 w-52 bg-[#050313] border border-white/15 rounded shadow-[0_18px_35px_rgba(0,0,0,0.9)] z-50">
                  <div className="p-4">
                    <div className="flex items-center gap-3">
                      {initials ? (
                        <div className="h-10 w-10 rounded-full bg-purple-500 text-white flex items-center justify-center font-semibold text-sm">
                          {initials}
                        </div>
                      ) : (
                        <div className="h-10 w-10 rounded-full bg-white/10 flex items-center justify-center">
                          <FiUser className="text-gray-100" />
                        </div>
                      )}

                      <div>
                        <div className="text-sm font-semibold text-white">
                          {user
                            ? `${user.firstName || ""} ${
                                user.lastName || ""
                              }`.trim()
                            : "Guest"}
                        </div>
                        <div className="text-xs text-gray-400 capitalize">
                          {user?.role || ""}
                        </div>
                      </div>
                    </div>

                    <div className="mt-3 border-t border-white/10 pt-3">
                      <button
                        onClick={() => {
                          setOpenProfile(false);
                          navigate("/account-details");
                        }}
                        className="w-full text-left px-2 py-2 text-sm text-gray-100 hover:bg-white/5 rounded"
                      >
                        Account
                      </button>

                      <button
                        onClick={handleLogout}
                        className="w-full mt-2 text-left px-2 py-2 text-sm text-red-300 hover:bg-red-500/10 rounded"
                      >
                        Logout
                      </button>
                    </div>
                  </div>
                </div>
              )}
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}
