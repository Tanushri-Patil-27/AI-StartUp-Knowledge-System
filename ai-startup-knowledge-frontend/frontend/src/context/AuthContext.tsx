import { createContext, useContext, useMemo, useState, type ReactNode } from "react";
import { api } from "../lib/api";
import { clearSession, getStoredUser, saveSession } from "../lib/storage";
import type { AuthUser, LoginRequest, LoginResponse } from "../types";

interface AuthContextValue {
  user: AuthUser | null;
  isAuthenticated: boolean;
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => getStoredUser());

  async function login(credentials: LoginRequest) {
    const { data } = await api.post<LoginResponse>("/auth/login", credentials);
    if (!data?.token) throw new Error("Login response did not include a JWT token.");
    saveSession(data);
    const { token: _token, ...authUser } = data;
    setUser(authUser);
  }

  function logout() {
    clearSession();
    setUser(null);
  }

  const value = useMemo(() => ({
    user,
    isAuthenticated: Boolean(user),
    login,
    logout,
  }), [user]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within AuthProvider");
  return context;
}
