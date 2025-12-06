import React from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useDispatch } from "react-redux";
import { setUser } from "../redux/reducers/userAuthReducer";
import axios from "axios";
import { useNavigate, Link } from "react-router-dom";
import { jwtDecode } from "jwt-decode";
import { setAuthToken } from "../api/api";

const loginSchema = z.object({
  email: z.string().email("Please enter a valid email"),
  password: z.string().min(6, "Password must be at least 6 characters"),
});

const Login = () => {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(loginSchema),
  });

  const loginSuccess = (token) => {
    const accessToken = token?.accessToken ?? null;
    const refreshToken = token?.refreshToken ?? null;

    if (!accessToken) {
      toast.error("Login succeeded but access token missing.");
      return;
    }

    let tokenDecoded = null;
    try {
      tokenDecoded = jwtDecode(accessToken);
    } catch (err) {
      console.error("JWT decode failed:", err);
    }

    if (tokenDecoded) {
      dispatch(setUser(tokenDecoded));
    }

    localStorage.setItem("accessToken", accessToken);
    if (refreshToken) localStorage.setItem("refreshToken", refreshToken);
    localStorage.setItem("token", accessToken); // legacy

    // use helper to set headers
    setAuthToken(accessToken);

    toast.success("Login Successful");
    setTimeout(() => {
      navigate("/");
    }, 2500);
  };

  const loginFailed = (msg) => toast.error(`Login failed: ${msg}`);

  const onSubmit = async (data) => {
    try {
      const response = await axios.post("http://localhost:8081/auth/login", {
        email: data.email,
        password: data.password,
      });

      const token = response.data;
      loginSuccess(token);
    } catch (error) {
      if (error.response) {
        loginFailed(error.response.data.message || "Invalid credentials");
      } else {
        loginFailed("Network or server error");
      }
      console.error("Login error:", error);
    }
  };

  return (
    <>
      <div className="min-h-screen flex items-center justify-center px-4">
        <div className="w-full max-w-sm rounded-3xl border border-white/10 bg-white/5 p-8 shadow-[0_18px_45px_rgba(0,0,0,0.65)] backdrop-blur">
          <div className="flex flex-col items-center justify-center mb-6">
            <div className="inline-flex items-center gap-2 rounded-full border border-purple-400/40 bg-purple-500/10 px-3 py-1 text-[11px] font-semibold uppercase tracking-[0.18em] text-purple-200">
              <span className="h-1.5 w-1.5 rounded-full bg-purple-300" />
              Welcome back
            </div>
            <h1 className="mt-4 text-3xl font-extrabold tracking-tight text-white">
              Eventix
            </h1>
            <p className="mt-2 text-xs text-gray-300">
              Sign in to discover and manage impact events.
            </p>
          </div>

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
            <div>
              <label className="block text-xs font-medium text-gray-200 mb-1.5">
                Email
              </label>
              <input
                type="email"
                {...register("email")}
                className="w-full rounded-xl border border-white/20 bg-black/30 px-3 py-2 text-sm text-white outline-none placeholder:text-gray-500 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                placeholder="you@example.com"
              />
              {errors.email && (
                <p className="mt-1 text-xs text-red-300">
                  {errors.email.message}
                </p>
              )}
            </div>

            <div>
              <label className="block text-xs font-medium text-gray-200 mb-1.5">
                Password
              </label>
              <input
                type="password"
                {...register("password")}
                className="w-full rounded-xl border border-white/20 bg-black/30 px-3 py-2 text-sm text-white outline-none placeholder:text-gray-500 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                placeholder="••••••••"
              />
              {errors.password && (
                <p className="mt-1 text-xs text-red-300">
                  {errors.password.message}
                </p>
              )}
            </div>

            <button
              type="submit"
              className="w-full rounded-full bg-purple-500 py-2 text-sm font-semibold text-white shadow-md shadow-purple-500/40 transition hover:bg-purple-400"
            >
              Sign In
            </button>
          </form>

          <p className="mt-6 text-center text-xs text-gray-300">
            Not an existing user?{" "}
            <Link
              to="/register"
              className="font-semibold text-purple-200 hover:text-purple-100 hover:underline"
            >
              Create one
            </Link>
          </p>
        </div>
      </div>

      <ToastContainer position="top-center" autoClose={2000} />
    </>
  );
};

export default Login;
