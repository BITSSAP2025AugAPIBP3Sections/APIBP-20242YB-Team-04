// src/api/api.js
import axios from "axios";
import { refreshAccessToken } from "./auth";

/** Axios instances */
export const searchApi = axios.create({
  baseURL: "http://localhost:8084/api/v1",
  timeout: 10000,
});

export const eventsApi = axios.create({
  baseURL: "http://localhost:8083/api/v1",
  timeout: 10000,
});

/** Helper to set / clear Authorization header for all API instances */
export function setAuthToken(token) {
  if (token) {
    const header = `Bearer ${token}`;
    searchApi.defaults.headers.common["Authorization"] = header;
    eventsApi.defaults.headers.common["Authorization"] = header;
  } else {
    delete searchApi.defaults.headers.common["Authorization"];
    delete eventsApi.defaults.headers.common["Authorization"];
  }
}

/**
 * Response interceptor to auto-refresh access token on 401 and retry the request.
 * Important: refreshAccessToken uses plain axios POST to auth service (not searchApi),
 * avoiding recursion into this interceptor.
 */
const apis = [searchApi, eventsApi];

apis.forEach((api) => {
  api.interceptors.response.use(
    (response) => response,
    async (error) => {
      const originalRequest = error.config;

      // Only attempt once per request
      if (error.response?.status === 401 && !originalRequest?._retry) {
        originalRequest._retry = true;

        const refreshToken = localStorage.getItem("refreshToken");
        if (!refreshToken) {
          return Promise.reject(error);
        }

        try {
          const newAccessToken = await refreshAccessToken(refreshToken);
          if (newAccessToken) {
            // persist and update headers
            localStorage.setItem("accessToken", newAccessToken);
            // update headers for all api instances
            searchApi.defaults.headers.common["Authorization"] = `Bearer ${newAccessToken}`;
            eventsApi.defaults.headers.common["Authorization"] = `Bearer ${newAccessToken}`;

            // set header on the original request and retry it
            originalRequest.headers = originalRequest.headers || {};
            originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;

            return api(originalRequest);
          }
        } catch (err) {
          console.error("Token refresh attempt failed in interceptor:", err);
          // fallthrough to reject
        }
      }

      return Promise.reject(error);
    }
  );
});
