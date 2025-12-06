// src/pages/Contact.jsx
import React, { useState } from "react";

const Contact = () => {
  const [form, setForm] = useState({
    name: "",
    email: "",
    topic: "general",
    message: "",
  });

  const [submitted, setSubmitted] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // TODO: wire to backend / email service
    console.log("Contact form submitted:", form);
    setSubmitted(true);
    setTimeout(() => setSubmitted(false), 3000);

    setForm({
      name: "",
      email: "",
      topic: "general",
      message: "",
    });
  };

  return (
    <div className="relative min-h-screen bg-gradient-to-b from-[#050313] via-[#050616] to-black text-white">
      {/* Soft background blobs */}
      <div className="pointer-events-none absolute inset-0 overflow-hidden">
        <div className="absolute -top-24 -left-10 h-64 w-64 rounded-full bg-purple-500/25 blur-3xl" />
        <div className="absolute top-40 right-0 h-72 w-72 rounded-full bg-pink-500/15 blur-3xl" />
        <div className="absolute bottom-0 left-1/3 h-64 w-64 rounded-full bg-indigo-500/15 blur-3xl" />
      </div>

      <div className="relative z-10 px-4 pb-16 pt-10">
        <div className="mx-auto max-w-5xl">
          {/* Header */}
          <section className="mb-8 text-center">
            <div className="inline-flex items-center gap-2 rounded-full border border-purple-400/40 bg-purple-500/10 px-3 py-1 text-[11px] font-semibold uppercase tracking-[0.18em] text-purple-200">
              <span className="h-1.5 w-1.5 rounded-full bg-purple-300" />
              Contact Eventix
            </div>

            <h1 className="mt-4 text-3xl font-bold leading-snug md:text-4xl">
              Let&apos;s make your next event
              <span className="block text-purple-200">mean something.</span>
            </h1>

            <p className="mx-auto mt-4 max-w-2xl text-sm text-gray-200/90 md:text-[0.98rem]">
              Whether you&apos;re organising a cause–driven event, looking to
              volunteer, or just curious about Eventix, we&apos;re here to
              listen and help.
            </p>
          </section>

          {/* Layout: left contact cards, right form */}
          <section className="grid gap-8 md:grid-cols-[1.1fr_1.2fr]">
            {/* Contact info cards */}
            <div className="space-y-5">
              <div className="rounded-3xl border border-white/10 bg-white/5 p-6 shadow-md backdrop-blur">
                <h2 className="text-sm font-semibold uppercase tracking-[0.18em] text-purple-100">
                  General enquiries
                </h2>
                <p className="mt-3 text-sm text-gray-200 leading-relaxed">
                  Have a question about how Eventix works or want to share
                  feedback? Reach out anytime.
                </p>
                <p className="mt-4 text-sm text-purple-100">
                  📧{" "}
                  <a
                    href="mailto:support@eventix.com"
                    className="font-medium text-purple-200 hover:underline"
                  >
                    support@eventix.com
                  </a>
                </p>
              </div>

              <div className="rounded-3xl border border-purple-400/40 bg-purple-600/15 p-6 shadow-md backdrop-blur">
                <h2 className="text-sm font-semibold uppercase tracking-[0.18em] text-purple-100">
                  Organisers & partners
                </h2>
                <p className="mt-3 text-sm text-purple-50 leading-relaxed">
                  Want to list your events on Eventix, collaborate on a
                  campaign or explore a partnership?
                </p>
                <p className="mt-4 text-sm text-purple-100">
                  🤝{" "}
                  <a
                    href="mailto:partners@eventix.com"
                    className="font-medium text-purple-50 hover:underline"
                  >
                    partners@eventix.com
                  </a>
                </p>
              </div>

              <div className="rounded-3xl border border-indigo-400/40 bg-indigo-500/15 p-6 shadow-md backdrop-blur">
                <h2 className="text-sm font-semibold uppercase tracking-[0.18em] text-indigo-100">
                  Technical support
                </h2>
                <p className="mt-3 text-sm text-indigo-50 leading-relaxed">
                  Stuck with login, bookings or dashboard? Share a brief
                  description and a screenshot if possible.
                </p>
                <p className="mt-4 text-sm text-indigo-100">
                  🛠{" "}
                  <a
                    href="mailto:tech@eventix.com"
                    className="font-medium text-indigo-50 hover:underline"
                  >
                    tech@eventix.com
                  </a>
                </p>
              </div>
            </div>

            {/* Contact form */}
            <div className="rounded-3xl border border-white/10 bg-white/5 p-7 shadow-[0_18px_45px_rgba(0,0,0,0.45)] backdrop-blur">
              <h2 className="text-lg font-semibold text-white">
                Send us a message
              </h2>
              <p className="mt-2 text-xs text-gray-300">
                We usually respond within 1–2 working days.
              </p>

              <form onSubmit={handleSubmit} className="mt-5 space-y-4">
                <div className="grid gap-4 md:grid-cols-2">
                  <div>
                    <label className="block text-xs font-medium text-gray-200 mb-1.5">
                      Name
                    </label>
                    <input
                      type="text"
                      name="name"
                      value={form.name}
                      onChange={handleChange}
                      required
                      className="w-full rounded-xl border border-white/20 bg-black/30 px-3 py-2 text-sm text-white outline-none placeholder:text-gray-500 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                      placeholder="Your name"
                    />
                  </div>

                  <div>
                    <label className="block text-xs font-medium text-gray-200 mb-1.5">
                      Email
                    </label>
                    <input
                      type="email"
                      name="email"
                      value={form.email}
                      onChange={handleChange}
                      required
                      className="w-full rounded-xl border border-white/20 bg-black/30 px-3 py-2 text-sm text-white outline-none placeholder:text-gray-500 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                      placeholder="you@example.com"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-medium text-gray-200 mb-1.5">
                    Topic
                  </label>
                  <select
                    name="topic"
                    value={form.topic}
                    onChange={handleChange}
                    className="w-full rounded-xl border border-white/20 bg-black/30 px-3 py-2 text-sm text-white outline-none focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                  >
                    <option value="general">General enquiry</option>
                    <option value="organiser">I&apos;m an organiser</option>
                    <option value="volunteer">I&apos;m a volunteer</option>
                    <option value="technical">Technical issue</option>
                    <option value="partnership">Partnership / collaboration</option>
                  </select>
                </div>

                <div>
                  <label className="block text-xs font-medium text-gray-200 mb-1.5">
                    Message
                  </label>
                  <textarea
                    name="message"
                    value={form.message}
                    onChange={handleChange}
                    required
                    rows={5}
                    className="w-full rounded-xl border border-white/20 bg-black/30 px-3 py-2 text-sm text-white outline-none placeholder:text-gray-500 focus:border-purple-400 focus:ring-1 focus:ring-purple-400"
                    placeholder="Tell us how we can help you..."
                  />
                </div>

                <div className="flex items-center justify-between pt-2">
                  <p className="text-[11px] text-gray-400 max-w-xs">
                    By submitting this form you agree to be contacted by the
                    Eventix team about your enquiry.
                  </p>

                  <button
                    type="submit"
                    className="rounded-full bg-purple-500 px-5 py-2 text-sm font-semibold text-white shadow-md shadow-purple-500/40 transition hover:bg-purple-400"
                  >
                    {submitted ? "Message sent ✓" : "Send message"}
                  </button>
                </div>
              </form>
            </div>
          </section>

          {/* Optional footer strip */}
          <section className="mt-10 rounded-3xl border border-white/5 bg-white/5 p-5 text-center text-xs text-gray-300 backdrop-blur">
            Prefer social? You can also add links here later for Instagram,
            LinkedIn or X once your public handles are live.
          </section>
        </div>
      </div>
    </div>
  );
};

export default Contact;
