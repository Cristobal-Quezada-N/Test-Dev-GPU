export interface User {
  id: string
  name: string
  email: string
  role: 'admin' | 'user'
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
  // #MOCK
  // refreshToken: string
}
