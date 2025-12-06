// src/pages/About.jsx
import React from "react";
import { useNavigate } from "react-router-dom";

const About = () => {
  const navigate = useNavigate();

  return (
    <div className="relative min-h-screen bg-gradient-to-b from-[#0b0414] via-[#070815] to-black text-white">
      {/* Soft color blobs in background */}
      <div className="pointer-events-none absolute inset-0 overflow-hidden">
        <div className="absolute -top-24 -left-16 h-64 w-64 rounded-full bg-purple-500/20 blur-3xl" />
        <div className="absolute top-40 right-0 h-72 w-72 rounded-full bg-pink-500/10 blur-3xl" />
        <div className="absolute bottom-0 left-1/3 h-64 w-64 rounded-full bg-indigo-500/10 blur-3xl" />
      </div>

      <div className="relative z-10 px-4 pb-16 pt-10">
        <div className="mx-auto max-w-6xl">
          {/* Hero Section */}
          <section className="mb-10 grid gap-8 md:grid-cols-[1.7fr_1.1fr]">
            {/* Left: copy */}
            <div className="rounded-3xl border border-white/10 bg-white/5 p-7 shadow-[0_18px_45px_rgba(0,0,0,0.45)] backdrop-blur">
              <div className="inline-flex items-center gap-2 rounded-full border border-purple-400/40 bg-purple-500/10 px-3 py-1 text-[11px] font-semibold uppercase tracking-[0.18em] text-purple-200">
                <span className="h-1.5 w-1.5 rounded-full bg-purple-300" />
                About Eventix
              </div>

              <h1 className="mt-4 text-3xl font-bold leading-snug md:text-4xl">
                We remember events by what we feel,
                <span className="block text-purple-200">
                  but we remember people by how they show up.
                </span>
              </h1>

              <p className="mt-4 max-w-xl text-sm text-gray-200/90 md:text-[0.98rem]">
                Eventix exists for organisers and volunteers who believe that
                time, energy and passion can change lives. We bring together
                people, causes and communities so that every event has a purpose
                – and every effort counts.
              </p>

              <div className="mt-6 flex flex-wrap gap-3">
                <button
                  onClick={() => navigate("/events")}
                  className="rounded-full bg-purple-500 px-5 py-2 text-sm font-semibold text-white shadow-md shadow-purple-500/30 transition hover:bg-purple-400"
                >
                  Explore events
                </button>
                <button
                  onClick={() => navigate("/login")}
                  className="rounded-full border border-white/20 bg-white/5 px-5 py-2 text-sm font-semibold text-purple-100 transition hover:border-purple-300/60 hover:bg-purple-500/10"
                >
                  Become a volunteer
                </button>
              </div>
            </div>

            {/* Right: “card” with mission bullets */}
            <div className="flex flex-col gap-4">
              <div className="rounded-3xl border border-purple-500/40 bg-gradient-to-br from-purple-600/40 via-purple-500/20 to-indigo-500/30 p-5 shadow-lg backdrop-blur">
                <p className="text-xs font-semibold uppercase tracking-[0.2em] text-purple-100">
                  Our mission
                </p>
                <p className="mt-3 text-[0.95rem] leading-relaxed text-purple-50">
                  <span className="font-semibold">
                    To connect organisers who care with volunteers who want to
                    contribute.
                  </span>{" "}
                  Whether you’re a nonprofit, a community organiser or a
                  volunteer, Eventix helps you find each other, organise better,
                  and impact more.
                </p>
              </div>

              <div className="rounded-3xl border border-white/10 bg-black/30 p-5 shadow-md backdrop-blur">
                <p className="text-xs font-semibold uppercase tracking-[0.2em] text-gray-300">
                  Built for impact
                </p>
                <p className="mt-3 text-[0.95rem] leading-relaxed text-gray-200">
                  We focus on events that mean something – fundraisers,
                  workshops, drives, campaigns and initiatives that leave people
                  and places better than before.
                </p>
              </div>
            </div>
          </section>

          {/* Stats strip (you can wire real numbers later) */}
          <section className="mb-10 grid gap-4 rounded-3xl border border-white/10 bg-white/5 p-4 text-center text-xs text-gray-200 shadow-[0_18px_45px_rgba(0,0,0,0.4)] backdrop-blur sm:grid-cols-3 sm:text-sm">
            <div className="flex flex-col items-center justify-center gap-1 border-b border-white/10 pb-3 sm:border-b-0 sm:border-r sm:pb-0 sm:pr-3">
              <p className="text-[11px] uppercase tracking-[0.18em] text-purple-200">
                For organisers
              </p>
              <p className="text-[0.9rem] text-gray-100">
                Publish events, manage capacity, track volunteers.
              </p>
            </div>
            <div className="flex flex-col items-center justify-center gap-1 border-b border-white/10 py-3 sm:border-b-0 sm:border-r sm:py-0 sm:px-3">
              <p className="text-[11px] uppercase tracking-[0.18em] text-purple-200">
                For volunteers
              </p>
              <p className="text-[0.9rem] text-gray-100">
                Discover meaningful ways to show up and give back.
              </p>
            </div>
            <div className="flex flex-col items-center justify-center gap-1 pt-3 sm:border-0 sm:pt-0 sm:pl-3">
              <p className="text-[11px] uppercase tracking-[0.18em] text-purple-200">
                For communities
              </p>
              <p className="text-[0.9rem] text-gray-100">
                Stronger neighbourhoods built one event at a time.
              </p>
            </div>
          </section>

          {/* What we do */}
          <section className="mb-10 grid gap-6 md:grid-cols-2">
            <div className="rounded-3xl border border-white/10 bg-white/5 p-7 shadow-md backdrop-blur">
              <h2 className="text-lg font-semibold text-white">
                What we do for you
              </h2>
              <p className="mt-3 text-sm text-gray-200 leading-relaxed">
                Eventix is a simple, organised home for cause–driven events.
                From blood donation camps and education workshops to clean-up
                drives and tech for good meetups – we make it easy to discover
                what’s happening around you.
              </p>

              <div className="mt-5 space-y-3 text-sm text-gray-100">
                <div className="flex gap-2">
                  <span className="mt-[3px] h-4 w-4 flex-shrink-0 rounded-full bg-purple-400/80" />
                  <div>
                    <p className="font-semibold text-white">
                      🗓 Discover impact events
                    </p>
                    <p className="text-gray-200">
                      Filter by city, category and interests to find events that
                      actually resonate with you.
                    </p>
                  </div>
                </div>

                <div className="flex gap-2">
                  <span className="mt-[3px] h-4 w-4 flex-shrink-0 rounded-full bg-pink-400/80" />
                  <div>
                    <p className="font-semibold text-white">
                      🤝 Connect organisers & volunteers
                    </p>
                    <p className="text-gray-200">
                      Clear event info, simple bookings and structured
                      communication – so everyone knows how to help.
                    </p>
                  </div>
                </div>
              </div>
            </div>

            <div className="grid gap-5">
              <div className="rounded-3xl border border-purple-400/40 bg-purple-500/10 p-6 shadow-md backdrop-blur">
                <h3 className="text-sm font-semibold text-purple-100">
                  📍 Make local action simple
                </h3>
                <p className="mt-2 text-sm text-purple-50">
                  Whether it&apos;s a neighbourhood clean-up or a large
                  city-wide campaign, Eventix helps you find events near you,
                  understand what&apos;s needed and show up prepared.
                </p>
              </div>

              <div className="rounded-3xl border border-indigo-400/40 bg-indigo-500/10 p-6 shadow-md backdrop-blur">
                <h3 className="text-sm font-semibold text-indigo-100">
                  📊 Support sustainable impact
                </h3>
                <p className="mt-2 text-sm text-indigo-50">
                  Over time, Eventix aims to give organisers insight into what
                  works, so they can grow their communities and keep volunteers
                  engaged for the long term.
                </p>
              </div>
            </div>
          </section>

          {/* Why we created Eventix + quote */}
          <section className="grid gap-8 md:grid-cols-[1.5fr_1.1fr]">
            <div className="rounded-3xl border border-white/10 bg-white/5 p-7 shadow-md backdrop-blur">
              <h2 className="text-lg font-semibold text-white">
                Why we created Eventix
              </h2>
              <p className="mt-3 text-sm text-gray-200 leading-relaxed">
                So many people want to help, but don&apos;t know where or how.
                At the same time, organisers spend endless hours on calls,
                spreadsheets and messages just to coordinate volunteers and fill
                events.
              </p>
              <p className="mt-3 text-sm text-gray-200 leading-relaxed">
                Eventix is our attempt to bridge that gap – a platform where
                good causes are easier to discover, logistics are less painful,
                and volunteering feels intentional, organised and rewarding.
              </p>
            </div>

            <div className="flex items-stretch">
              <div className="flex w-full flex-col justify-between rounded-3xl border border-purple-300/40 bg-gradient-to-br from-purple-700/60 via-purple-600/40 to-fuchsia-600/40 p-7 text-purple-50 shadow-xl backdrop-blur">
                <div>
                  <p className="text-xs font-semibold uppercase tracking-[0.2em] text-purple-100/90">
                    Our promise
                  </p>
                  <p className="mt-3 text-sm leading-relaxed">
                    We want Eventix to be purpose–first, transparent and human.
                    Every event listed should give someone a chance to show up,
                    contribute, and go home feeling like they made a difference.
                  </p>
                </div>

                <p className="mt-5 text-center text-[0.9rem] italic text-purple-50/90">
                  “We make a living by what we do, but we make a life by who we
                  lift up along the way.”
                </p>
              </div>
            </div>
          </section>
        </div>
      </div>
    </div>
  );
};

export default About;
