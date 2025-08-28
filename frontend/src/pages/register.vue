<template>
  <div class="login-container">
    <v-card class="login-card elevation-12">
      <div class="login-header text-center pa-6">
        <v-avatar size="80" color="primary" class="mb-4">
          <v-icon size="40" color="white">mdi-account-plus</v-icon>
        </v-avatar>
        <h1 class="text-h4 font-weight-bold text-primary mb-2">
          Registrarse
        </h1>
      </div>

      <v-card-text class="pa-6">
        <v-form @submit.prevent="handleRegister">
          <v-text-field
            v-model="authStore.registerForm.email"
            label="Correo Electrónico"
            type="email"
            variant="outlined"
            prepend-inner-icon="mdi-email"
            :rules="authStore.emailRules"
            required
            class="mb-4"
            autocomplete="username"
          ></v-text-field>

          <v-text-field
            v-model="authStore.registerForm.password"
            label="Contraseña"
            :type="authStore.showPassword ? 'text' : 'password'"
            variant="outlined"
            prepend-inner-icon="mdi-lock"
            :append-inner-icon="authStore.showPassword ? 'mdi-eye-off' : 'mdi-eye'"
            @click:append-inner="authStore.showPassword = !authStore.showPassword"
            :rules="authStore.passwordRules"
            required
            class="mb-6"
            autocomplete="new-password"
          ></v-text-field>

          <v-btn
            block
            color="primary"
            size="large"
            type="submit"
            :loading="authStore.loading"
            :disabled="authStore.loading"
          >
            <v-icon left>mdi-account-plus</v-icon>
            Registrarse
          </v-btn>
        </v-form>
      </v-card-text>

      <v-card-actions class="pa-6 pt-0">
        <div class="text-center w-100">
          <p class="text-caption text-medium-emphasis">
            ¿Ya tienes una cuenta?
            <v-btn
              variant="text"
              color="primary"
              size="small"
              @click="$router.push('/login')"
            >
              Inicia sesión aquí
            </v-btn>
          </p>
        </div>
      </v-card-actions>
    </v-card>

    <v-snackbar v-model="authStore.showError" color="error" timeout="5000" location="top">
      {{ authStore.errorMessage }}
      <template v-slot:actions>
        <v-btn variant="text" @click="authStore.clearError">Cerrar</v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
const authStore = useAuthStore()

definePage({
  meta: {
    layout: 'auth',
    requiresAuth: false,
  },
})


const handleRegister = () => {
  authStore.handleRegister()
}
</script>
