import axios from "axios";

const api = axios.create({
  baseURL: "/",
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  async (config) => {
    let token = localStorage.getItem("authToken");
    const refreshToken = localStorage.getItem("refreshToken");

    if (token) {
      const tokenPayload = JSON.parse(atob(token.split(".")[1]));
      const isTokenExpired = tokenPayload.exp * 1000 < Date.now();

      if (isTokenExpired && refreshToken) {
        try {
          const response = await axios.post("/auth/refresh", { refreshToken });
          const { access_token, refresh_token } = response.data;

          localStorage.setItem("authToken", access_token);
          localStorage.setItem("refreshToken", refresh_token);

          config.headers.Authorization = `Bearer ${access_token}`;
        } catch (error) {
          console.error("Failed to refresh token", error);
          localStorage.removeItem("authToken");
          localStorage.removeItem("refreshToken");
          return Promise.reject(error);
        }
      } else {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }

    return config;
  },
  (error) => Promise.reject(error)
);

export default api;
