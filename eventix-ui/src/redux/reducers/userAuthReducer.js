import { createSlice } from "@reduxjs/toolkit";
import initialUserAuthState from "./userAuthReducerInit";

const userAuthReducer = createSlice({
  name: "userAuth",
  initialState: initialUserAuthState,
  reducers: {
    setToken: (state, action) => {
      state.token = action.payload;
    },
    clearToken: (state) => {
      state.token = null;
    },
  },
});

export const { setToken, clearToken } = userAuthReducer.actions;
export default userAuthReducer.reducer;