import { defineStore } from 'pinia'
import { reactive, ref } from 'vue'
import router from '@/router'
import { authService } from '@/services/auth/auth.services'
import { type LoginForm } from '@/types/auth.types'
import { useAppStore } from '@/stores/app'
import { type User } from '@/types/auth.types'

const registerForm = reactive({
  email: '',
  password: '',
})

export const useAuthStore = defineStore('auth', () => {
  const appStore = useAppStore()

  // State
  const loading = ref(false)
  const showPassword = ref(false)
  const showError = ref(false)
  const errorMessage = ref('')
  const isInitialized = ref(false)

  // Form data
  const loginForm = reactive<LoginForm>({
    email: '',
    password: '',
    rememberMe: false,
  })

  // Validation rules
  const emailRules = [
    (v: string) => !!v || 'El correo electrónico es requerido',
    (v: string) => /.+@.+\..+/.test(v) || 'El correo electrónico debe ser válido',
  ]

  const passwordRules = [
    (v: string) => !!v || 'La contraseña es requerida',
    (v: string) => v.length >= 6 || 'La contraseña debe tener al menos 6 caracteres',
  ]

  // Token management
  const getStoredToken = (): string | null => {
    return localStorage.getItem('auth_token')
  }

  const setStoredToken = (token: string): void => {
    localStorage.setItem('auth_token', token)
  }

  const removeStoredToken = (): void => {
    localStorage.removeItem('auth_token')
  }

  const getStoredUser = (): User | null => {
    const userStr = localStorage.getItem('auth_user')
    return userStr ? JSON.parse(userStr) : null
  }

  const setStoredUser = (user: User): void => {
    localStorage.setItem('auth_user', JSON.stringify(user))
  }

  const removeStoredUser = (): void => {
    localStorage.removeItem('auth_user')
  }

  // Authentication methods
  const initializeAuth = async (): Promise<void> => {
    if (isInitialized.value) {
      return
    }

    try {
      const token = getStoredToken()
      const storedUser = getStoredUser()
      if (token && storedUser) {
        // Validate stored token
        appStore.login(storedUser)
      }
    } catch (error: unknown) {
      console.warn('Auth initialization failed, clearing stored data:', error)
      // Clear invalid stored data
      removeStoredToken()
      removeStoredUser()
    } finally {
      isInitialized.value = true
    }
    isInitialized.value = true
  }

  const handleLogin = async (): Promise<void> => {
    loading.value = true
    errorMessage.value = ''

    try {
      const response = await authService.login(loginForm.email, loginForm.password)

      const token = response.auth_token
      const user = JSON.parse(response.auth_user)
      // Store tokens and user data
      setStoredToken(token)
      setStoredUser(user)

      // Update app store
      appStore.login(user)

      // Reset form
      resetForm()
    } catch (error) {
      errorMessage.value = error instanceof Error ? error.message : 'Error al iniciar sesión'
      showError.value = true
    } finally {
      loading.value = false
    }
  }

  const logout = (): void => {
    // Clear stored data
    removeStoredToken()
    removeStoredUser()

    // Update app store
    appStore.logout()

    // Reset form
    resetForm()

    // Clear errors
    clearError()
  }

  const forgotPassword = (): void => {
    // Implement forgot password functionality
    console.log('Forgot password clicked')
  }

  const register = (): void => {
    // Implement registration functionality
    console.log('Register clicked')
  }

  const handleRegister = async () => {
    loading.value = true
    clearError()

    try {
      const res = await authService.register(registerForm.email, registerForm.password, 2)
      console.log(res.message)

      resetRegisterForm()
      router.push('/login')
    } catch (error: unknown) {
      console.error(' Error en register:', error)
      errorMessage.value = error instanceof Error ? error.message : 'Error desconocido'
      showError.value = true
    } finally {
      loading.value = false
    }
  }

  const clearError = (): void => {
    showError.value = false
    errorMessage.value = ''
  }

  const resetForm = (): void => {
    loginForm.email = ''
    loginForm.password = ''
    loginForm.rememberMe = false
  }

  const resetRegisterForm = () => {
    registerForm.email = ''
    registerForm.password = ''
  }

  return {
    // State
    loading,
    showPassword,
    showError,
    errorMessage,
    loginForm,
    registerForm,
    isInitialized,

    // Validation
    emailRules,
    passwordRules,

    // Methods
    initializeAuth,
    handleLogin,
    handleRegister,
    logout,
    forgotPassword,
    register,
    clearError,
    resetForm,
    setStoredToken,
    setStoredUser,
  }
})
