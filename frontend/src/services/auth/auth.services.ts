import { authApi } from './auth.api'

export const authService = {
  login: async (email: string, password: string) => {
  // Para #MOCK
  //   const data = await authApi.mockLogin(email, password)
  //   return data
  // },
    const response = await authApi.post('/api/auth/login', { email, password })
    return response.data // { token: "..."}
  },
  register: async (email: string, password: string) => {
    const response = await authApi.post('/api/auth/register', { email, password })
    return response.data
  },
}
