import React from "react";
import { useForm } from "react-hook-form";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import axios from "axios";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate, Link } from "react-router-dom";

const signupSchema = z.object({
  firstName: z.string().min(1, "First name is required"),
  lastName: z.string().min(1, "Last name is required"),
  phoneNumber: z
    .string()
    .min(10, "Phone number must be at least 10 digits"),
  email: z.string().email("Please enter a valid email"),
  password: z.string().min(6, "Password must be at least 6 characters"),
});

const Signup = () => {
  const navigate = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm({
    resolver: zodResolver(signupSchema),
  });

  const onSubmit = async (data) => {
    const payload = {
      firstName: data.firstName,
      lastName: data.lastName,
      phoneNumber: data.phoneNumber,
      email: data.email,
      password: data.password,
    };

    try {
      await axios.post("http://localhost:8081/users/register", payload);
      toast.success("Signup successful! Please login.");
      reset();
      setTimeout(() => navigate("/login"), 3000);
    } catch (error) {
      toast.error(
        error.response?.data?.message || "Signup failed. Please try again."
      );
      console.error(error);
    }
  };

  return (
    <>
      <div className="min-h-screen flex items-center justify-center px-4">
        <div className="w-full max-w-md rounded-3xl border border-white/10 bg-white/5 p-8 shadow-[0_18px_45px_rgba(0,0,0,0.65)] backdrop-blur">
          {/* Header */}
          <div className="mb-6 text-center">
            <div className="inline-flex items-center gap-2 rounded-full border border-purple-400/40 bg-purple-500/10 px-3 py-1 text-[11px] font-semibold uppercase tracking-[0.18em] text-purple-200">
              <span className="h-1.5 w-1.5 rounded-full bg-purple-300" />
              Join Eventix
            </div>
            <h1 className="mt-4 text-3xl font-extrabold text-white">
              Eventix
            </h1>
            <p className="mt-2 text-xs text-gray-300">
              Create your account and start discovering events with a purpose.
            </p>
          </div>

          {/* Form */}
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div className="grid gap-4 md:grid-cols-2">
              <div>
                <label className="mb-1.5 block text-xs font-medium text-gray-200">
                  First Name
                </label>
                <input
                  type="text"
                  {...register("firstName")}
                  className="w-full rounded-xl border border-white/20 bg-black/30 px-3 py-2 text-sm text-white outline-none placeholder:text-gray-500 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                  placeholder="John"
                />
                {errors.firstName && (
                  <p className="mt-1 text-xs text-red-300">
                    {errors.firstName.message}
                  </p>
                )}
              </div>

              <div>
                <label className="mb-1.5 block text-xs font-medium text-gray-200">
                  Last Name
                </label>
                <input
                  type="text"
                  {...register("lastName")}
                  className="w-full rounded-xl border border-white/20 bg-black/30 px-3 py-2 text-sm text-white outline-none placeholder:text-gray-500 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                  placeholder="Doe"
                />
                {errors.lastName && (
                  <p className="mt-1 text-xs text-red-300">
                    {errors.lastName.message}
                  </p>
                )}
              </div>
            </div>

            <div>
              <label className="mb-1.5 block text-xs font-medium text-gray-200">
                Phone Number
              </label>
              <input
                type="text"
                {...register("phoneNumber")}
                className="w-full rounded-xl border border-white/20 bg-black/30 px-3 py-2 text-sm text-white outline-none placeholder:text-gray-500 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                placeholder="9876543210"
              />
              {errors.phoneNumber && (
                <p className="mt-1 text-xs text-red-300">
                  {errors.phoneNumber.message}
                </p>
              )}
            </div>

            <div>
              <label className="mb-1.5 block text-xs font-medium text-gray-200">
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
              <label className="mb-1.5 block text-xs font-medium text-gray-200">
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
              className="mt-2 w-full rounded-full bg-purple-500 py-2 text-sm font-semibold text-white shadow-md shadow-purple-500/40 transition hover:bg-purple-400"
            >
              Sign Up
            </button>
          </form>

          <p className="mt-6 text-center text-xs text-gray-300">
            Already a user?{" "}
            <Link
              to="/login"
              className="font-semibold text-purple-200 hover:text-purple-100 hover:underline"
            >
              Login here
            </Link>
          </p>
        </div>
      </div>

      <ToastContainer position="top-center" autoClose={2000} />
    </>
  );
};

export default Signup;
