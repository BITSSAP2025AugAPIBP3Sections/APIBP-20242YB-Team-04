import React from "react";
import { useForm } from "react-hook-form";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import axios from "axios";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { useNavigate } from "react-router-dom";

const signupSchema = z.object({
  firstName: z.string().min(1, "First name is required"),
  lastName: z.string().min(1, "Last name is required"),
  phoneNumber: z.string().min(10, "Phone number must be at least 10 digits"),
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
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-100 to-white">
      <div className="w-full max-w-md bg-white p-8 rounded-2xl shadow-xl border border-blue-200">

        <h1 className="text-3xl font-extrabold text-blue-700 text-center mb-4">
          Eventix
        </h1>

        <h2 className="text-xl font-semibold text-gray-800 mb-4 text-center">
          Create Your Account
        </h2>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">

          <div>
            <label className="block text-gray-700 text-sm font-medium mb-1">
              First Name
            </label>
            <input
              type="text"
              {...register("firstName")}
              className="w-full px-4 py-2 border rounded-lg shadow-sm focus:ring-2 focus:ring-blue-400 focus:outline-none"
              placeholder="John"
            />
            {errors.firstName && (
              <p className="text-xs text-red-600 mt-1">{errors.firstName.message}</p>
            )}
          </div>

          <div>
            <label className="block text-gray-700 text-sm font-medium mb-1">
              Last Name
            </label>
            <input
              type="text"
              {...register("lastName")}
              className="w-full px-4 py-2 border rounded-lg shadow-sm focus:ring-2 focus:ring-blue-400 focus:outline-none"
              placeholder="Doe"
            />
            {errors.lastName && (
              <p className="text-xs text-red-600 mt-1">{errors.lastName.message}</p>
            )}
          </div>

          <div>
            <label className="block text-gray-700 text-sm font-medium mb-1">
              Phone Number
            </label>
            <input
              type="text"
              {...register("phoneNumber")}
              className="w-full px-4 py-2 border rounded-lg shadow-sm focus:ring-2 focus:ring-blue-400 focus:outline-none"
              placeholder="9876543210"
            />
            {errors.phoneNumber && (
              <p className="text-xs text-red-600 mt-1">
                {errors.phoneNumber.message}
              </p>
            )}
          </div>

          <div>
            <label className="block text-gray-700 text-sm font-medium mb-1">
              Email
            </label>
            <input
              type="email"
              {...register("email")}
              className="w-full px-4 py-2 border rounded-lg shadow-sm focus:ring-2 focus:ring-blue-400 focus:outline-none"
              placeholder="you@example.com"
            />
            {errors.email && (
              <p className="text-xs text-red-600 mt-1">{errors.email.message}</p>
            )}
          </div>

          <div>
            <label className="block text-gray-700 text-sm font-medium mb-1">
              Password
            </label>
            <input
              type="password"
              {...register("password")}
              className="w-full px-4 py-2 border rounded-lg shadow-sm focus:ring-2 focus:ring-blue-400 focus:outline-none"
              placeholder="••••••••"
            />
            {errors.password && (
              <p className="text-xs text-red-600 mt-1">{errors.password.message}</p>
            )}
          </div>

          <button
            type="submit"
            className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-2 rounded-lg shadow-md transition duration-300"
          >
            Sign Up
          </button>
        </form>

        <p className="text-xs text-center text-gray-500 mt-6">
          Already a user?{" "}
          <a href="/login" className="text-blue-600 hover:underline">
            Login here
          </a>
        </p>
      </div>

      <ToastContainer position="top-center" autoClose={2000} />
    </div>
  );
};

export default Signup;
