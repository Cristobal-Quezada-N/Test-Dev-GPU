import { authApi } from './auth.api'

export const authService = {
  login: async (email: string, password: string) => {

    // REAL
    const response = await authApi.login({ email, password })

    const user = {
      ...response.user,
      role: response.user.roleId === 1 ? 'admin' : 'user',
    }

    return {
      ...response,
      user,
    }
  },

  register: async (email: string, password: string, roleId: number) => {
    // REAL
    const response = await authApi.register({ email, password, roleId })
    


    return {
      ...response,
    }
  },
}
