/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        primary: "#4CAF50",
        secondary: "#388E3C",
        accent: "#E8F5E9",
        neutral: "#BDBDBD",
      },
    },
  },
  plugins: [],
};
