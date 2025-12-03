// src/components/Navbar.jsx
import { useState, useEffect, useRef } from "react";
import axios from "axios";
import { useSelector, useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { FiSearch, FiMapPin, FiUser } from "react-icons/fi";
import { clearUser } from "../redux/reducers/userAuthReducer";

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
        const res = await axios.get("http://localhost:8084/api/v1/search/filters");
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

  // Close dropdowns/popovers on outside click or Escape
  useEffect(() => {
    const handleDocClick = (e) => {
      if (cityDropdownRef.current && !cityDropdownRef.current.contains(e.target)) {
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
      }
    };

    document.addEventListener("click", handleDocClick);
    document.addEventListener("keydown", handleEsc);
    return () => {
      document.removeEventListener("click", handleDocClick);
      document.removeEventListener("keydown", handleEsc);
    };
  }, []);

  const handleLogout = () => {
    dispatch(clearUser());
    localStorage.removeItem("token");
    // keep city if you want, or remove:
    // localStorage.removeItem("eventix_city");
    setOpenProfile(false);
    navigate("/login");
  };

  return (
    <nav className="w-full bg-white border-b border-gray-200">
      <div className="max-w-8xl mx-auto px-6 py-3 flex items-center justify-between">
        {/* LEFT SECTION */}
        <div className="flex items-center gap-6">
          <h1 className="text-2xl font-bold text-purple-600">Eventix</h1>

          <div className="h-6 w-[1px] bg-gray-300" />

          {/* City selector */}
          <div className="relative" ref={cityDropdownRef}>
            <button
              type="button"
              onClick={() => setOpenCity((s) => !s)}
              className="flex items-center gap-2 text-gray-900 font-semibold px-3 py-1 rounded hover:bg-gray-50 focus:outline-none"
              aria-expanded={openCity}
              aria-haspopup="listbox"
            >
              <FiMapPin className="text-purple-600" size={16} />
              <div className="text-left">
                <div className="text-sm">{selectedCity}</div>
                <p className="text-xs text-gray-500 -mt-1">Region</p>
              </div>
            </button>

            {openCity && (
              <div className="absolute z-40 mt-2 w-56 bg-white border border-gray-200 rounded shadow-lg">
                <div className="p-3 text-xs text-gray-500">Select city</div>

                <div className="max-h-60 overflow-auto">
                  {loadingCities && (
                    <div className="px-3 py-2 text-sm text-gray-600">Loading cities...</div>
                  )}

                  {!loadingCities && cityError && (
                    <div className="px-3 py-2 text-sm text-red-600">{cityError}</div>
                  )}

                  {!loadingCities && !cityError && cities.length === 0 && (
                    <div className="px-3 py-2 text-sm text-gray-600">No cities available</div>
                  )}

                  {!loadingCities &&
                    !cityError &&
                    cities.map((c) => (
                      <button
                        key={c}
                        onClick={() => chooseCity(c)}
                        className={`w-full text-left px-3 py-2 text-sm hover:bg-gray-50 ${
                          c === selectedCity
                            ? "bg-purple-50 text-purple-700 font-semibold"
                            : "text-gray-700"
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

        {/* CENTER MENU */}
        <div className="hidden md:flex items-center gap-6 ml-10">
          {menu.map((item) => (
            <button
              key={item.name}
              className={`px-3 py-1 text-sm font-semibold transition ${
                item.active
                  ? "bg-purple-100 text-purple-700 rounded-full"
                  : "text-gray-600 hover:text-gray-900"
              }`}
            >
              {item.name}
            </button>
          ))}
        </div>

        {/* RIGHT SECTION */}
        <div className="flex items-center gap-6">
          {/* Search Icon */}
          <FiSearch className="text-purple-600 cursor-pointer" size={20} />

          {/* Login Button OR Profile */}
          {!user ? (
            <button
              onClick={() => navigate("/login")}
              className="px-4 py-1.5 text-sm font-semibold text-purple-600 border border-purple-500 rounded-full hover:bg-purple-50 transition"
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
                  <div className="h-9 w-9 rounded-full bg-purple-600 text-white flex items-center justify-center font-semibold">
                    {initials}
                  </div>
                ) : (
                  <div className="h-9 w-9 rounded-full bg-gray-200 flex items-center justify-center">
                    <FiUser className="text-gray-700" size={18} />
                  </div>
                )}
              </button>

              {/* Profile Popover */}
              {openProfile && (
                <div className="absolute right-0 mt-2 w-52 bg-white border border-gray-200 rounded shadow-lg z-50">
                  <div className="p-4">
                    <div className="flex items-center gap-3">
                      {initials ? (
                        <div className="h-10 w-10 rounded-full bg-purple-600 text-white flex items-center justify-center font-semibold text-sm">
                          {initials}
                        </div>
                      ) : (
                        <div className="h-10 w-10 rounded-full bg-gray-200 flex items-center justify-center">
                          <FiUser className="text-gray-600" />
                        </div>
                      )}

                      <div>
                        <div className="text-sm font-semibold text-gray-900">
                          {user ? `${user.firstName || ""} ${user.lastName || ""}`.trim() : "Guest"}
                        </div>
                        <div className="text-xs text-gray-500 capitalize">{user?.role || ""}</div>
                      </div>
                    </div>

                    <div className="mt-3 border-t border-gray-100 pt-3">
                      <button
                        onClick={() => {
                          setOpenProfile(false);
                          navigate("/account-details");
                        }}
                        className="w-full text-left px-2 py-2 text-sm text-gray-700 hover:bg-gray-50 rounded"
                      >
                        Account
                      </button>

                      <button
                        onClick={handleLogout}
                        className="w-full mt-2 text-left px-2 py-2 text-sm text-red-600 hover:bg-red-50 rounded"
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
