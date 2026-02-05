import type { LoginForm } from '@/types/auth.types'
import { defineStore } from 'pinia'
import { reactive, ref } from 'vue'
import router from '@/router'
import { authService } from '@/services/auth/auth.services'
import { useAppStore } from '@/stores/app'
import { authStorage } from './auth.storage'
import { authValidation } from './auth.validation'

const registerForm = reactive({
  email: '',
  password: '',
})

export const authStore = defineStore('auth', () => {
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

  // Authentication methods
  const initializeAuth = async (): Promise<void> => {
    if (isInitialized.value) {
      return
    }

    try {
      const token = authStorage.getToken()
      const storedUser = authStorage.getUser()
      if (token && storedUser) {
        // Validate stored token
        appStore.login(storedUser)
      }
    } catch (error: unknown) {
      console.warn('Auth initialization failed, clearing stored data:', error)
      // Clear invalid stored data
      authStorage.clearAll()
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
      authStorage.setToken(token)
      authStorage.setUser(user)

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
    authStorage.clearAll()

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

    // Validation rules
    emailRules: authValidation.emailRules,
    passwordRules: authValidation.passwordRules,

    // Methods
    initializeAuth,
    handleLogin,
    handleRegister,
    logout,
    forgotPassword,
    register,
    clearError,
    resetForm,
  }
})
