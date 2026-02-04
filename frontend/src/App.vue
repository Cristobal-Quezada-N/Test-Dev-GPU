<template>
  <v-snackbar
    v-model="notificationStore.show"
    :color="notificationStore.color"
    :timeout="notificationStore.timeout"
  >
    {{ notificationStore.message }}

    <template #actions>
      <v-btn color="white" text @click="notificationStore.close()">
        Cerrar
      </v-btn>
    </template>
  </v-snackbar>
  <LoadingScreen v-if="!useAuthStore.isInitialized" />
  <router-view v-else />
</template>

<script setup lang="ts">
  import { onMounted } from 'vue'
  import LoadingScreen from '@/components/LoadingScreen.vue'
  import { useAppStore } from '@/stores/app'
  import { useNotificationStore } from '@/stores/notification'
import { authStore } from '@/stores/auth/auth.store'

  const authStore = useAuthStore()
  const appStore = useAppStore()
  const notificationStore = useNotificationStore()

  onMounted(async () => {
    if (!authStore.isInitialized) {
      appStore.setLoading(true)
      try {
        await authStore.initializeAuth()
      } catch (error) {
        console.error('Auth initialization error:', error)
      } finally {
        appStore.setLoading(false)
      }
    }
  })
</script>
