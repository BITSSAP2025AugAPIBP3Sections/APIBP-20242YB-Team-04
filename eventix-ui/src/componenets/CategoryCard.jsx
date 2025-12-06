// src/components/CategoryCard.jsx
import React from "react";

export default function CategoryCard({ label, Icon, onClick }) {
  const initial = label?.[0]?.toUpperCase() || "?";

  return (
    <button
      onClick={() => onClick(label)}
      className="group relative flex flex-col items-center justify-center gap-3 w-44 h-44 rounded-full border border-white/10 bg-white/5 p-4 shadow-[0_9px_35px_rgba(0,0,0,0.6)] backdrop-blur-sm transition transform hover:-translate-y-1 hover:shadow-[0_24px_50px_rgba(0,0,0,0.8)] focus:outline-none"
      aria-label={`Category ${label}`}
      title={label}
    >
      {/* Glowing ring accent */}
      <div className="absolute inset-0 rounded-2xl bg-gradient-to-br from-purple-600/20 via-purple-700/10 to-indigo-500/20 opacity-0 group-hover:opacity-100 transition-opacity blur-md" />

      {/* Inner icon bubble */}
      <div className="relative z-10 flex h-24 w-24 items-center justify-center rounded-full bg-gradient-to-br from-purple-500/40 to-indigo-500/40 shadow-lg shadow-purple-500/20 backdrop-blur text-white group-hover:scale-110 transition-transform duration-300">
        {Icon ? (
          <Icon size={38} className="drop-shadow-[0_2px_4px_rgba(0,0,0,0.6)]" />
        ) : (
          <span className="text-2xl font-bold">{initial}</span>
        )}
      </div>

      {/* Label text */}
      <div className="relative z-10 w-full px-1 text-[0.82rem] font-semibold text-gray-100 text-center truncate group-hover:text-purple-200 transition">
        {label}
      </div>
    </button>
  );
}
