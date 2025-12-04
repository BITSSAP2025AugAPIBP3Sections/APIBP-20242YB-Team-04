// src/api/auth.js
import axios from "axios";

/**
 * Call refresh endpoint to get a new access token.
 * Returns the new access token string on success, otherwise null.
 *
 * NOTE: URL should match your auth server.
 */
export async function refreshAccessToken(refreshToken) {
  if (!refreshToken) return null;

  try {
    const res = await axios.post("http://localhost:8081/auth/refresh", {
      refreshToken,
    }, {
      timeout: 8000,
    });

    // assume response: { accessToken: "...", refreshToken?: "..." }
    const newAccessToken = res.data?.accessToken ?? null;
    const newRefreshToken = res.data?.refreshToken ?? null;

    // if backend returns refreshed refreshToken too, update storage
    if (newRefreshToken) {
      localStorage.setItem("refreshToken", newRefreshToken);
    }

    return newAccessToken;
  } catch (err) {
    console.error("refreshAccessToken error:", err);
    return null;
  }
}
