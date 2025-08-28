// #MOCK
import type { MockUser } from '@/services/auth/auth.types'
// Utilities
import { defineStore } from 'pinia'
import { reactive, ref } from 'vue'
import { useAppStore } from './app'
import { authService } from '@/services/auth/auth.services'


export interface LoginForm {
  email: string
  password: string
  rememberMe: boolean
}

const registerForm = reactive({
  email: '',
  password: '',
})

export interface AuthResponse {
  user: {
    id: string
    name: string
    email: string
    role: 'admin' | 'user'
    avatar?: string
  }
  token: string
  // #MOCK
  refreshToken: string
}

export const useAuthStore = defineStore('auth', () => {
  const appStore = useAppStore()

  // State
  const loading = ref(false)
  const demoLoading = ref<string | null>(null)
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

  // #MOCK
  const mockUsers: MockUser[] = [
    {
      id: '1',
      name: 'Administrador',
      email: 'admin@demo.com',
      password: 'admin123',
      role: 'admin',
      avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
    },
    {
      id: '2',
      name: 'Usuario Demo',
      email: 'user@demo.com',
      password: 'user123',
      role: 'user',
      avatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
    },
    {
      id: '3',
      name: 'Juan Pérez',
      email: 'juan@demo.com',
      password: 'juan123',
      role: 'user',
      avatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
    },
  ]

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

  const getStoredUser = (): any => {
    const userStr = localStorage.getItem('auth_user')
    return userStr ? JSON.parse(userStr) : null
  }

  const setStoredUser = (user: any): void => {
    localStorage.setItem('auth_user', JSON.stringify(user))
  }

  const removeStoredUser = (): void => {
    localStorage.removeItem('auth_user')
  }

  // #MOCK
  const mockApiCall = async (delay = 1000): Promise<void> => {
    return new Promise(resolve => {
      setTimeout(resolve, delay)
    })
  }

  const mockLogin = async (email: string, password: string): Promise<AuthResponse> => {
    // Simulate API delay
    await mockApiCall(1500)

    // Find user in mock data
    const user = mockUsers.find(u => u.email === email && u.password === password)

    if (!user) {
      throw new Error('Credenciales inválidas')
    }

    // Generate mock tokens
    const token = `mock_token_${user.id}_${Date.now()}`
    const refreshToken = `mock_refresh_${user.id}_${Date.now()}`

    return {
      user: {
        id: user.id,
        name: user.name,
        email: user.email,
        role: user.role,
        avatar: user.avatar,
      },
      token,
      refreshToken,
    }
  }

  const mockValidateToken = async (token: string): Promise<any> => {
    // Simulate API delay
    await mockApiCall(500)

    // Extract user ID from mock token
    const tokenParts = token.split('_')
    if (tokenParts.length < 3) {
      throw new Error('Token inválido')
    }

    const userId = tokenParts[2]
    const user = mockUsers.find(u => u.id === userId)

    if (!user) {
      throw new Error('Usuario no encontrado')
    }

    return {
      id: user.id,
      name: user.name,
      email: user.email,
      role: user.role,
      avatar: user.avatar,
    }
  }

  // Authentication methods
  const initializeAuth = async (): Promise<void> => {
    if (isInitialized.value) {
      return
    }

    // #MOCK
    try {
      const token = getStoredToken()
      const storedUser = getStoredUser()
      if (token && storedUser) {
      // Validate stored token
        const user = await mockValidateToken(token)
        appStore.login(user)
      }
    } catch (error) {
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
      // #MOCK
      const response = await mockLogin(loginForm.email, loginForm.password)
      // const response = await authService.login(loginForm.email, loginForm.password)

      // Store tokens and user data
      setStoredToken(response.token)
      setStoredUser(response.user)

      // Update app store
      appStore.login(response.user)

      // Reset form
      resetForm()
    } catch (error) {
      errorMessage.value = error instanceof Error ? error.message : 'Error al iniciar sesión'
      showError.value = true
    } finally {
      loading.value = false
    }
  }

  // #MOCK
  const demoLogin = async (role: 'user' | 'admin'): Promise<void> => {
    demoLoading.value = role

    try {
      const demoUser = mockUsers.find(u => u.role === role)
      if (!demoUser) {
        throw new Error('Usuario de demostración no encontrado')
      }

      const response = await mockLogin(demoUser.email, demoUser.password)

      // Store tokens and user data
      setStoredToken(response.token)
      setStoredUser(response.user)

      // Update app store
      appStore.login(response.user)
    } catch (error) {
      errorMessage.value = error instanceof Error ? error.message : 'Error al iniciar sesión de demostración'
      showError.value = true
    } finally {
      demoLoading.value = null
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
    const res = await authService.register(registerForm.email, registerForm.password)
    console.log(res.message)

    resetRegisterForm()
    router.push('/login')
  } catch (err: any) {
  console.error(" Error en register:", err)

  if (err.response) {
    errorMessage.value = err.response.data.message || 'Error al registrarse'
  } else {
    errorMessage.value = err.message || 'Error desconocido'
  }

  showError.value = true
}finally {
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
    demoLoading,
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
    // #MOCK
    initializeAuth,
    demoLogin,
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
