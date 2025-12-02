import React from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import { ToastContainer, toast } from "react-toastify";
import { SiCashapp } from "react-icons/si";
import "react-toastify/dist/ReactToastify.css";
import { useDispatch } from "react-redux";
import { setUser } from "../redux/reducers/userAuthReducer";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

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
    const tokenDecoded = token ? jwtDecode(token.accessToken) : null;
    const role = tokenDecoded ? tokenDecoded.role : null;
    console.log("Decoded Token:", tokenDecoded);
    dispatch(setUser(tokenDecoded)); 

    toast.success("Login Successful");
    localStorage.setItem("token", token.accessToken);

    // setTimeout(() => {
    //   role === "MANAGER" ? navigate("/manager-dashboard") : navigate("/account-details");
    // }, 2500);
  };
  const loginFailed = (msg) => toast.error(`Login failed: ${msg}`);

  const onSubmit = async (data) => {
    try {
      const response = await axios.post(
        "http://localhost:8081/auth/login",
        {
          email: data.email,
          password: data.password,
        }
      );

      const token = response.data;
      // console.log(token);
      
      // dispatch(setUser(token)); // Save token in Redux
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
      <div className="min-h-screen flex items-center justify-center bg-gray-100">
        <div className="w-full max-w-sm bg-white border border-gray-300 rounded-md p-8 shadow-md">
          <div className="flex items-center justify-center mb-6">
            <h1 className="ml-2 text-3xl font-extrabold text-blue-700 tracking-tight">
              Eventix
            </h1>
          </div>
          <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
            <div>
              <label className="block text-sm text-gray-700 mb-1">Email</label>
              <input
                type="email"
                {...register("email")}
                className="w-full px-3 py-2 border border-gray-300 rounded-sm text-sm focus:outline-none focus:ring-1 focus:ring-blue-600 focus:border-blue-600"
              />
              {errors.email && (
                <p className="text-xs text-red-600 mt-1">
                  {errors.email.message}
                </p>
              )}
            </div>
            <div>
              <label className="block text-sm text-gray-700 mb-1">
                Password
              </label>
              <input
                type="password"
                {...register("password")}
                className="w-full px-3 py-2 border border-gray-300 rounded-sm text-sm focus:outline-none focus:ring-1 focus:ring-blue-600 focus:border-blue-600"
              />
              {errors.password && (
                <p className="text-xs text-red-600 mt-1">
                  {errors.password.message}
                </p>
              )}
            </div>
            <button
              type="submit"
              className="w-full py-2 bg-blue-600 text-white text-sm font-medium rounded-sm hover:bg-blue-700 transition"
            >
              Sign In
            </button>
          </form>
          <p className="text-xs text-center text-gray-500 mt-6">
            Not an existing user{" "}
            <a href="/register" className="text-blue-600 hover:underline">
              create here
            </a>
          </p>
        </div>
      </div>
      <ToastContainer position="top-center" autoClose={2000} />
    </>
  );
};

export default Login;