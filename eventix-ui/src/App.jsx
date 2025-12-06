import React from "react";
import Navbar from "./componenets/Navbar";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Signup from "./pages/SignUp";
import Dashboard from "./pages/Dashboard";
import EventsPage from "./pages/EventsPage";
import AuthInitializer from "./init/AuthInitializer";
import EventDetail from "./pages/EventDetail";
import MyBookings from "./pages/MyBookings";
import About from "./pages/About";
import Contact from "./pages/Contact";

function App() {

  return (
    <>
      <BrowserRouter>
      <AuthInitializer />
        <div className="min-h-screen flex flex-col">
          <Navbar />
          <main className="flex-grow">
            <Routes>
              <Route path="/" element={<Dashboard/>} />
              <Route path="/events" element={<EventsPage/>} />
              <Route path="/events/:id" element={<EventDetail />} />
              <Route path="/my-bookings" element={<MyBookings />} />
              <Route path="/about" element={<About />} />
              <Route path="/contact" element={<Contact />} />


              {/* <Route path="/" element={<Home />} /> */}
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Signup />} />
              {/* <Route
                path="/transactions"
                element={
                  <AuthRoute>
                    <Passbook />
                  </AuthRoute>
                }
              />
              <Route
                path="/account-details"
                element={
                  <AuthRoute>
                    <AccountDetails />
                  </AuthRoute>
                }
              />
              <Route
                path="/account-request"
                element={
                  <AuthRoute>
                    <AccountRequest />
                  </AuthRoute>
                }
              />
              <Route
                path="/account-request-list"
                element={
                  <AuthRoute>
                    <UserAccountRequest />
                  </AuthRoute>
                }
              />
              <Route
                path="/loan-account-details"
                element={
                  <AuthRoute>
                    <LoanAccountDetails />
                  </AuthRoute>
                }
              />
              <Route
                path="/money-transfer"
                element={
                  <AuthRoute>
                    <TransactionForm />
                  </AuthRoute>
                }
              />
              <Route
                path="/loan-applications"
                element={<LoanApplicaitonList />}
              />
              <Route path="/apply-loan" element={<LoanApplicationForm />} />
              <Route
                path="/account-request-application-list"
                element={
                  <AuthRoute>
                    <ManagerRoute>
                      <AccountRequestList />
                    </ManagerRoute>
                  </AuthRoute>
                }
              />
              <Route
                path="/loan-request-application-list"
                element={
                  <AuthRoute>
                    <ManagerRoute>
                      <LoanRequestList />
                    </ManagerRoute>
                  </AuthRoute>
                }
              />
              <Route
                path="/manager-dashboard"
                element={
                  <AuthRoute>
                    <ManagerRoute>
                      <ManagerDashboard />
                    </ManagerRoute>
                  </AuthRoute>
                }
              /> */}
            </Routes>
          </main>
          {/* <Footer /> */}
        </div>
      </BrowserRouter>
    </>
  );
}

export default App
