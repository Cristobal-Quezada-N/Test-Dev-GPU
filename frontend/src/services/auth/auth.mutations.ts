import { useMutation } from '@tanstack/vue-query'
import { useAppStore } from '@/stores/app'
import { authStorage } from '@/stores/auth/auth.storage'
import queryClient from '../query.client'
import { authService } from './auth.services'

export function useLogin() {
  const appStore = useAppStore()
  return useMutation({
    mutationFn: (data: any) => authService.login(data.email, data.password),
    onSuccess: data => {
      authStorage.setToken(data.token)
      authStorage.setUser(data.user)
      appStore.login(data.user)
    },
  }, queryClient)
}

export function useRegister () {
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
