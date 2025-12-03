// src/redux/slices/citySlice.js
import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  city: localStorage.getItem("eventix_city") || "",
};

const citySlice = createSlice({
  name: "city",
  initialState,
  reducers: {
    setCity(state, action) {
      state.city = action.payload;
      localStorage.setItem("eventix_city", action.payload);
    },
    clearCity(state) {
      state.city = "";
      localStorage.removeItem("eventix_city");
    },
  },
});

export const { setCity, clearCity } = citySlice.actions;
export default citySlice.reducer;
