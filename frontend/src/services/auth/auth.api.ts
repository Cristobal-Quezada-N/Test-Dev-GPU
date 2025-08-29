import client from '../api.client'
import { AUTH_API } from './auth.defaults'

export const authApi = {
  login: async (data: any) => {
    return (await client.post(AUTH_API.login, data)).data
  },

  register: async (data: any) => {
    return (await client.post(AUTH_API.register, data)).data
  },
}
