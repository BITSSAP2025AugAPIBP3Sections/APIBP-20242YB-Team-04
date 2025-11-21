import { configureStore } from "@reduxjs/toolkit";
import userAuthReducer from "../reducers/userAuthReducer";

export const store = configureStore({
  reducer: {
    userAuth: userAuthReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
  devTools: true,
});