import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";

export default defineConfig({
  plugins: [react()],

  server: {
    host: "0.0.0.0",
    port: 5173,
    strictPort: true,

    allowedHosts: [
      ".cloudshell.dev",
      ".cloudshell.googleusercontent.com",
    ],

    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,

        configure: (proxy) => {
          proxy.on("proxyReq", (proxyReq, req) => {
            const token = req.headers["x-pustakaalay-token"];

            if (token) {
              proxyReq.setHeader(
                "Authorization",
                `Bearer ${token}`
              );

              proxyReq.removeHeader("x-pustakaalay-token");
            }
          });
        },
      },
    },
  },
});
