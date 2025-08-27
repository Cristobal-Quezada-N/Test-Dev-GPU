/**
 * router/index.ts
 *
 * Automatic routes for `./src/pages/*.vue`
 */

import { setupLayouts } from 'virtual:generated-layouts'
// Composables
import { createRouter, createWebHistory } from 'vue-router/auto'
import { routes } from 'vue-router/auto-routes'
import { useAppStore } from '@/stores/app'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: setupLayouts(routes),
})

// Authentication guard
router.beforeEach((to, from, next) => {
  const appStore = useAppStore()

  // Check if route requires authentication
  const requiresAuth = to.meta.requiresAuth !== false // Default to true if not specified

  // If route doesn't require auth, allow access
  if (!requiresAuth) {
    // If user is already authenticated and trying to access login, redirect to home
    if (appStore.isAuthenticated && to.path === '/login') {
      next('/')
      return
    }
    next()
    return
  }

  // Check if user is authenticated for protected routes
  if (!appStore.isAuthenticated // Only redirect if we're not already going to login
    && to.path !== '/login') {
    next('/login')
    return
  }

  next()
})

// Workaround for https://github.com/vitejs/vite/issues/11804
router.onError((err, to) => {
  if (err?.message?.includes?.('Failed to fetch dynamically imported module')) {
    if (localStorage.getItem('vuetify:dynamic-reload')) {
      console.error('Dynamic import error, reloading page did not fix it', err)
    } else {
      console.log('Reloading page to fix dynamic import error')
      localStorage.setItem('vuetify:dynamic-reload', 'true')
      location.assign(to.fullPath)
    }
  } else {
    console.error(err)
  }
})

router.isReady().then(() => {
  localStorage.removeItem('vuetify:dynamic-reload')
})

export default router
