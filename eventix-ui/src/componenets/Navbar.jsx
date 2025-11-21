import { useState } from "react";
import { FiSearch } from "react-icons/fi";
import { FiMapPin } from "react-icons/fi";
import { FiUser } from "react-icons/fi";

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

  return (
    <nav className="w-full bg-white border-b border-gray-200">
      <div className="max-w-8xl mx-auto px-6 py-3 flex items-center justify-between">
        {/* LEFT SECTION */}
        <div className="flex items-center gap-6">
          {/* Logo */}
          {/* <img 
            src="/logo.png" 
            alt="Eventix" 
            className="h-8 object-contain"
          /> */}
          <h1 className="text-2xl font-bold text-purple-600">Eventix</h1>

          {/* Vertical divider */}
          <div className="h-6 w-[1px] bg-gray-300"></div>

          {/* Location */}
          <div>
            <div className="flex items-center gap-1 text-gray-900 font-semibold">
              <FiMapPin className="text-purple-600" size={16} />
              <span>Gurugram</span>
            </div>
            <p className="text-sm text-gray-500 -mt-1">Haryana</p>
          </div>
        </div>

        {/* CENTER MENU */}
        <div className="hidden md:flex items-center gap-6 ml-10">
          {menu.map((item) => (
            <button
              key={item.name}
              className={`
                px-3 py-1 text-sm font-semibold transition
                ${
                  item.active
                    ? "bg-purple-100 text-purple-700 rounded-full"
                    : "text-gray-600 hover:text-gray-900"
                }
              `}
            >
              {item.name}
            </button>
          ))}
        </div>

        {/* RIGHT SECTION */}
        <div className="flex items-center gap-6">
          {/* Search Icon */}
          <FiSearch className="text-purple-600 cursor-pointer" size={20} />

          {/* User circle */}
          <div className="h-9 w-9 rounded-full bg-gray-200 flex items-center justify-center cursor-pointer">
            <FiUser className="text-gray-700" size={18} />
          </div>
        </div>
      </div>
    </nav>
  );
}
