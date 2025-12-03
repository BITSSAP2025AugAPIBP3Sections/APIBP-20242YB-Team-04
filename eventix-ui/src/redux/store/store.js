import { configureStore } from "@reduxjs/toolkit";
import userAuthReducer from "../reducers/userAuthReducer";
import citySlice from "../reducers/citySlice";

export const store = configureStore({
  reducer: {
    userAuth: userAuthReducer,
    city: citySlice,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
  devTools: true,
});