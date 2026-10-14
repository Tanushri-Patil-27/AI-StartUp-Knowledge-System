import type { AuthUser, LoginResponse } from "../types";

const TOKEN_KEY = "knowbase_token";
const USER_KEY = "knowbase_user";

export function saveSession(response: LoginResponse) {
  sessionStorage.setItem(TOKEN_KEY, response.token);
  const user = {
    userId: response.userId,
    name: response.name,
    email: response.email,
    role: response.role,
    organizationId: response.organizationId,
  };
  sessionStorage.setItem(USER_KEY, JSON.stringify(user));
}

export function getToken() {
  return sessionStorage.getItem(TOKEN_KEY);
}

export function getStoredUser(): AuthUser | null {
  const value = sessionStorage.getItem(USER_KEY);
  if (!value) return null;
  try {
    return JSON.parse(value) as AuthUser;
  } catch {
    return null;
  }
}

export function clearSession() {
  sessionStorage.removeItem(TOKEN_KEY);
  sessionStorage.removeItem(USER_KEY);
}
