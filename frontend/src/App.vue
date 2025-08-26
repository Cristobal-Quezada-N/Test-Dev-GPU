<template>
  <v-snackbar
    v-model="notificationStore.show"
    :timeout="notificationStore.timeout"
    :color="notificationStore.color"
  >
    {{ notificationStore.message }}

    <template #actions>
      <v-btn color="white" text @click="notificationStore.close()">Cerrar</v-btn>
    </template>
  </v-snackbar> 
  <LoadingScreen v-if="!authStore.isInitialized" />
  <router-view v-else />
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useAppStore } from '@/stores/app'
import LoadingScreen from '@/components/LoadingScreen.vue'
import { useNotificationStore } from '@/stores/notification'

const authStore = useAuthStore();
const appStore = useAppStore();
const notificationStore = useNotificationStore();

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
