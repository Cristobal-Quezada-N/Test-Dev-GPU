export interface User {
  id: string
  name: string
  email: string
  role: 'ADMIN' | 'USER'
  avatar?: string
}

export interface LoginForm {
  email: string
  password: string
  rememberMe: boolean
}

export interface AuthResponse {
  user: User
  token: string
  // refreshToken: string
}
