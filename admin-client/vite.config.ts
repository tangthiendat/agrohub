import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";
import dotenv from "dotenv";

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  // Load environment variables based on the current mode
  const env = loadEnv(mode, process.cwd());

  // Load the appropriate .env file
  dotenv.config({ path: `.env.${mode}` });

  return {
    plugins: [react()],
    server: {
      host: true,
      port: 5000,
    },
    define: {
      "process.env": env,
    },
  };
});
