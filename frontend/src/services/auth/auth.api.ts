import type { MockUser } from './auth.types'
import client from '../api.client'
import { AUTH_API, mockUsers } from './auth.defaults'

export const authApi = {
  login: async (data: any) => {
    return (await client.post(AUTH_API.login, data)).data
  },

  register: async (data: any) => {
    return (await client.post(AUTH_API.register, data)).data
  },

  mockLogin: async (email: string, password: string) => {
    // Simula delay de API
    await new Promise(resolve => setTimeout(resolve, 1500))

    // Busca usuario en mock
    const user = mockUsers.find(
      (u: MockUser) => u.email === email && u.password === password
    )

    if (!user) {
      throw new Error('Credenciales inválidas')
    }

    // Tokens mock
    const token = `mock_token_${user.id}_${Date.now()}`
    const refreshToken = `mock_refresh_${user.id}_${Date.now()}`

    return {
      user: {
        id: user.id,
        name: user.name,
        email: user.email,
        role: user.role,
        avatar: user.avatar,
      },
      token,
      refreshToken,
    }
  },
}
