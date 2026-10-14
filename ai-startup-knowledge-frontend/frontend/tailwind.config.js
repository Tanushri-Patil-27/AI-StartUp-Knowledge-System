/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        ink: "#101827",
        panel: "#172235",
        accent: "#7C6CF2",
        mint: "#72E0C1"
      },
      boxShadow: {
        glow: "0 18px 60px rgba(35, 25, 100, 0.18)"
      }
    }
  },
  plugins: []
};
