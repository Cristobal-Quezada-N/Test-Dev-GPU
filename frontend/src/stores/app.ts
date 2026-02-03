import type { User } from '@/types/auth.types'
// Utilities
import { defineStore } from 'pinia'
import { computed, ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  // State
  const user = ref<User | null>(null)
  const isAuthenticated = ref(false)
  const isLoading = ref(false)
  const drawer = ref(true)

  // Computed
  const isAdmin = computed(() => user.value?.role === 'ADMIN')
  const isUser = computed(() => user.value?.role === 'USER')
  const userInitials = computed(() => {
    if (!user.value?.name) {
      return ''
    }
    return user.value.name
      .split(' ')
      .map(n => n[0])
      .join('')
      .toUpperCase()
      .slice(0, 2)
  })

  // Actions
  const login = (userData: User) => {
    user.value = userData
    isAuthenticated.value = true
  }

  const logout = () => {
    user.value = null
    isAuthenticated.value = false
  }

  const toggleDrawer = () => {
    drawer.value = !drawer.value
  }

  const setLoading = (loading: boolean) => {
    isLoading.value = loading
  }

  return {
    // State
    user,
    isAuthenticated,
    isLoading,
    drawer,

    // Computed
    isAdmin,
    isUser,
    userInitials,

    // Actions
    login,
    logout,
    toggleDrawer,
    setLoading,
  }
})
