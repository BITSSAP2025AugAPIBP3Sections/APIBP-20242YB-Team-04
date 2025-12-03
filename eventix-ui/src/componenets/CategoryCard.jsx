// src/components/CategoryCard.jsx
import React from "react";

export default function CategoryCard({ label, Icon, onClick }) {
  return (
    <button
      onClick={() => onClick(label)}
      className="flex flex-col items-center gap-3 p-4 w-48 h-48 bg-white rounded-full shadow-sm hover:shadow-md transition transform hover:-translate-y-0.5 focus:outline-none"
      aria-label={`Category ${label}`}
      title={label}
    >
      <div className="h-28 w-28 rounded-full bg-purple-50 flex items-center justify-center text-purple-600">
        {Icon ? <Icon size={35} /> : <span className="font-semibold text-base">{label?.[0]}</span>}
      </div>
      <div className="text-xs text-gray-700 text-center truncate w-full px-1">{label}</div>
    </button>
  );
}
