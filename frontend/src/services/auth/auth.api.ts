import type { MockUser } from './auth.types'
import client from '../api.client'
import { AUTH_API, mockUsers } from './auth.defaults'

export const authApi = {
  login: async (data: any) => {
    return (await client.post(AUTH_API.login, data)).data
  },
  mockLogin: async (email: string, password: string) => {
    // Simulate API delay
    await new Promise(resolve => setTimeout(resolve, 1500))

    // Find user in mock data
    const user = mockUsers.find((u: MockUser) => u.email === email && u.password === password)

    if (!user) {
      throw new Error('Credenciales inválidas')
    }

    // Generate mock tokens
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
