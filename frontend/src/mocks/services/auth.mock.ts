import { type User } from '@/types/auth.types'
import { mockUsers, type MockUser } from '@/mocks/data/users.mock'

const API_LOGIN_DELAY = 1500
const API_VALIDATE_DELAY = 500

export interface AuthResponse {
  user: User
  token: string
  refreshToken: string
}

interface DemoLoginCallbacks {
  setStoredToken: (token: string) => void
  setStoredUser: (user: User) => void
  onLoginSuccess: (user: User) => void
  setLoading: (role: string) => void
  setError: (message: string) => void
  onNavigate?: () => void
}

async function mockApiCall(delay: number): Promise<void> {
  return new Promise(resolve => {
    setTimeout(resolve, delay)
  })
}

async function mockLogin(email: string, password: string, delay: number = API_LOGIN_DELAY): Promise<AuthResponse> {
  // Simulate API delay
  await mockApiCall(delay)

  // Find user in mock data
  const user = mockUsers.find(u => u.email === email && u.password === password)

  if (!user) {
    throw new Error('Credenciales inválidas')
  }

  // Generate mock tokens
  const token = `mock_token_${user.id}_${Date.now()}`
  const refreshToken = `mock_refreshToken_${user.id}_${Date.now()}`

  return {
    user: {
      id: user.id,
      name: user.name,
      email: user.email,
      role: user.role,
      avatar: user.avatar,
    } as User,
    token,
    refreshToken,
  } as AuthResponse
}

function getMockUserByRole(role: 'USER' | 'ADMIN'): MockUser | undefined {
  return mockUsers.find(u => u.role === role);
}

export async function mockValidateToken(token: string): Promise<any> {
  // Simulate API delay
  await mockApiCall(API_VALIDATE_DELAY)

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
  } as User
}

export async function executeMockLogin(role: 'USER' | 'ADMIN', callbacks: DemoLoginCallbacks): Promise<void> {
  callbacks.setLoading(role)

  try {
    const demoUser = getMockUserByRole(role)

    if (!demoUser) {
      throw new Error('Usuario Mock no encontrado')
    }

    const response = await mockLogin(demoUser.email, demoUser.password)

    // Store tokens and user data
    callbacks.setStoredToken(response.token)
    callbacks.setStoredUser(response.user)

    // Update app store
    callbacks.onLoginSuccess(response.user)
  } catch (error) {
    const errorMessage = error instanceof Error ? error.message : 'Error al iniciar sesión de demostración'
    callbacks.setError(errorMessage)
  }
}

/* Uso:
 *    const demoLoading = ref<string | null>(null)
 *    ...
 *    const demoLogin = async (role: 'USER' | 'ADMIN'): Promise<void> => {
 *      await executeDemoLogin(role, {
 *        setStoredToken,
 *        setStoredUser,
 *        onLoginSuccess: (user: User) => appStore.login(user),
 *        setLoading: (value: string | null) => { demoLoading.value = value },
 *        setError: (message: string) => {
 *          errorMessage.value = message
 *          showError.value = true
 *        }
 *      })
 *    }
 *    ...
 *    return {
 *      ...
 *      demoLoading,
 *      demoLogin
 *    }
 */
