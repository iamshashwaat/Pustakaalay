const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "/api";

export async function apiRequest(endpoint, options = {}) {
  const token = localStorage.getItem("token");

  const headers = {
    "Content-Type": "application/json",
    ...options.headers,
  };

  /*
   * Cloud Shell Web Preview intercepts browser Authorization headers.
   * Send our token under an application-specific header instead.
   * Vite converts it back to Authorization before proxying to Spring Boot.
   */
  if (token) {
    if (import.meta.env.VITE_API_BASE_URL) {
      headers.Authorization = `Bearer ${token}`;
    } else {
      headers["X-Pustakaalay-Token"] = token;
    }
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  const contentType = response.headers.get("content-type");
  let data = null;

  if (contentType && contentType.includes("application/json")) {
    data = await response.json();
  } else {
    data = await response.text();
  }

  if (!response.ok) {
    throw new Error(
      data?.message || `Request failed with status ${response.status}`
    );
  }

  return data;
}

export function login(email, password) {
  return apiRequest("/auth/login", {
    method: "POST",
    body: JSON.stringify({ email, password }),
  });
}
