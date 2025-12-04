import { createSlice } from "@reduxjs/toolkit";
import initialUserAuthState from "./userAuthReducerInit";

const userAuthReducer = createSlice({
  name: "userAuth",
  initialState: initialUserAuthState,
  reducers: {
    setUser: (state, action) => {
      state.token = action.payload;
    },
    clearUser: (state) => {
      state.token = null;
    },
  },
});

export const { setUser, clearUser } = userAuthReducer.actions;
export default userAuthReducer.reducer;