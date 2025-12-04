// src/init/AuthInitializer.jsx
import React, { useEffect } from "react";
import { useDispatch } from "react-redux";

// tolerant jwt-decode import that works with different bundlers
import { jwtDecode } from "jwt-decode";

import { setUser, clearUser } from "../redux/reducers/userAuthReducer";
import { setAuthToken } from "../api/api";
import { refreshAccessToken } from "../api/auth";

/**
 * AuthInitializer mounts once and ensures Redux + axios are in sync with localStorage tokens.
 * Put <AuthInitializer /> near the top of your app (before routes).
 */
export default function AuthInitializer() {
  const dispatch = useDispatch();

  useEffect(() => {
    let mounted = true;

    const init = async () => {
      try {
        const accessToken = localStorage.getItem("accessToken");
        const refreshToken = localStorage.getItem("refreshToken");

        if (!accessToken) {
          // no access token, try to refresh if refreshToken exists
          if (refreshToken) {
            const newAccess = await refreshAccessToken(refreshToken);
            if (newAccess) {
              localStorage.setItem("accessToken", newAccess);
              setAuthToken(newAccess);
              const decoded = jwtDecode(newAccess);
              if (mounted) dispatch(setUser(decoded));
              return;
            }
          }
          // nothing worked
          setAuthToken(null);
          if (mounted) dispatch(clearUser());
          return;
        }

        // decode access token
        let decoded;
        try {
          decoded = jwtDecode(accessToken);
        } catch (err) {
          console.warn("Invalid access token in storage:", err);
          // try refresh
          if (refreshToken) {
            const newAccess = await refreshAccessToken(refreshToken);
            if (newAccess) {
              localStorage.setItem("accessToken", newAccess);
              setAuthToken(newAccess);
              const newDecoded = jwtDecode(newAccess);
              if (mounted) dispatch(setUser(newDecoded));
              return;
            }
          }
          setAuthToken(null);
          if (mounted) dispatch(clearUser());
          return;
        }

        // check expiry (exp is in seconds)
        const nowSec = Date.now() / 1000;
        if (decoded.exp && decoded.exp <= nowSec) {
          // access token expired -> try refresh
          if (!refreshToken) {
            setAuthToken(null);
            if (mounted) dispatch(clearUser());
            return;
          }

          const newAccess = await refreshAccessToken(refreshToken);
          if (!newAccess) {
            setAuthToken(null);
            if (mounted) dispatch(clearUser());
            return;
          }

          localStorage.setItem("accessToken", newAccess);
          setAuthToken(newAccess);
          const newDecoded = jwtDecode(newAccess);
          if (mounted) dispatch(setUser(newDecoded));
          return;
        }

        // token valid
        setAuthToken(accessToken);
        if (mounted) dispatch(setUser(decoded));
      } catch (err) {
        console.error("AuthInitializer error:", err);
        setAuthToken(null);
        if (mounted) dispatch(clearUser());
      }
    };

    init();

    return () => {
      mounted = false;
    };
  }, [dispatch]);

  return null; // invisible
}
