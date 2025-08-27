import { authApi } from './auth.api'

export const authService = {
  login: async (email: string, password: string) => {
    const data = await authApi.mockLogin(email, password)
    return data
  },
}
