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

export const useRegister = () => {
  const router = useRouter()
  const notificationStore = useNotificationStore()

  return useMutation({
    mutationFn: (data: { email: string, password: string }) =>
      authService.register(data.email, data.password),
    onSuccess: data => {
      notificationStore.notify(data.message || 'Usuario registrado correctamente', 'success')
      router.push('/login')
    },
    onError: (error: any) => {
      notificationStore.notify(error.response?.data?.message || error.message, 'error')
    },
  })
}
