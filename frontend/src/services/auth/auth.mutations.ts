import { useMutation } from '@tanstack/vue-query'
import { useAppStore } from '@/stores/app'
import { useAuthStore } from '@/stores/auth'
import queryClient from '../query.client'
import { authService } from './auth.services'

export const useLogin = () => {
  const authStore = useAuthStore()
  const appStore = useAppStore()
  return useMutation({
    mutationFn: (data: any) => authService.login(data.email, data.password),
    onSuccess: data => {
      authStore.setStoredToken(data.token)
      authStore.setStoredUser(data.user)
      appStore.login(data.user)
    },
  }, queryClient)
}
