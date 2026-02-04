import type { User } from '@/types/auth.types'

const AUTH_TOKEN_KEY = 'auth_token'
const AUTH_USER_KEY = 'auth_user'

export const authStorage = {
  // Token Management
  getToken (): string | null {
    return localStorage.getItem(AUTH_TOKEN_KEY)
  },

  setToken (token: string): void {
    localStorage.setItem(AUTH_TOKEN_KEY, token)
  },

  removeToken (): void {
    localStorage.removeItem(AUTH_TOKEN_KEY)
  },

  // User Management
  getUser (): User | null {
    const userStr = localStorage.getItem(AUTH_USER_KEY)
    return userStr ? JSON.parse(userStr) : null
  },

  setUser (user: User): void {
    localStorage.setItem(AUTH_USER_KEY, JSON.stringify(user))
  },

  removeUser (): void {
    localStorage.removeItem(AUTH_USER_KEY)
  },

  // Clear all auth data
  clearAll (): void {
    this.removeToken()
    this.removeUser()
  },
}
