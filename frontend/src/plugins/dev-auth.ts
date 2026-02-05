import { useAppStore } from '@/stores/app'

export function devBypass() {
  if (import.meta.env.DEV && import.meta.env.VITE_BYPASS_LOGIN === 'true') {
    const appStore = useAppStore()
    appStore.login({
      id: 'dev-admin',
      name: 'Developer Admin',
      email: 'admin@dev.local',
      role: 'ADMIN',
      avatar: ''
    })
    console.log('Dev environment: Admin session')
  }
}
