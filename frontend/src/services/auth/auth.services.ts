import { authApi } from './auth.api'

export const authService = {
  login: async (email: string, password: string) => {
    // MOCK
    // const data = await authApi.mockLogin(email, password)
    // return data

    // REAL
    return await authApi.login({ email, password })
  },

  register: async (email: string, password: string) => {
    // REAL
    return await authApi.register({ email, password })
  },
}
