<template>
  <div class="login-container">
    <!-- Background Pattern -->
    <div class="background-pattern" />

    <!-- Register Card -->
    <v-card class="login-card elevation-12">
      <!-- Header -->
      <div class="login-header text-center pa-6">
        <v-avatar size="80" color="primary" class="mb-4">
          <v-icon size="40" color="white">mdi-account-plus</v-icon>
        </v-avatar>
        <h1 class="text-h4 font-weight-bold text-primary mb-2">
          Registrarse
        </h1>
        <p class="text-body-1 text-medium-emphasis">
          Crea tu cuenta para gestionar tus préstamos
        </p>
      </div>

      <v-divider />

      <!-- Register Form -->
      <v-card-text class="pa-6">
        <v-form @submit.prevent="handleRegister">
          <!-- Email Field -->
          <v-text-field
            v-model="authStore.registerForm.email"
            autocomplete="username"
            class="mb-4"
            label="Correo Electrónico"
            prepend-inner-icon="mdi-email"
            required
            :rules="authStore.emailRules"
            type="email"
            variant="outlined"
          />

          <!-- Password Field -->
          <v-text-field
            v-model="authStore.registerForm.password"
            :append-inner-icon="authStore.showPassword ? 'mdi-eye-off' : 'mdi-eye'"
            autocomplete="new-password"
            class="mb-6"
            label="Contraseña"
            prepend-inner-icon="mdi-lock"
            required
            :rules="authStore.passwordRules"
            :type="authStore.showPassword ? 'text' : 'password'"
            variant="outlined"
            @click:append-inner="authStore.showPassword = !authStore.showPassword"
          />

          <!-- Register Button -->
          <v-btn
            block
            class="mb-4"
            color="primary"
            :disabled="authStore.loading"
            :loading="authStore.loading"
            size="large"
            type="submit"
          >
            <v-icon left>
              mdi-account-plus
            </v-icon>
            Registrarse
          </v-btn>
        </v-form>
      </v-card-text>

      <!-- Footer -->
      <v-card-actions class="pa-6 pt-0">
        <div class="text-center w-100">
          <p class="text-caption text-medium-emphasis">
            ¿Ya tienes una cuenta?
            <v-btn
              color="primary"
              size="small"
              variant="text"
              :to="{ path: '/login' }"
            >
              Inicia sesión aquí
            </v-btn>
          </p>
        </div>
      </v-card-actions>
    </v-card>

    <!-- Error Snackbar -->
    <v-snackbar
      v-model="authStore.showError"
      color="error"
      location="top"
      timeout="5000"
    >
      {{ authStore.errorMessage }}
      <template #actions>
        <v-btn
          variant="text"
          @click="authStore.clearError"
        >
          Cerrar
        </v-btn>
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

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #00A499 0%, #EA7600 100%);
  position: relative;
  overflow: hidden;
}

.background-pattern {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image:
    radial-gradient(circle at 25% 25%, rgba(255, 255, 255, 0.1) 0%, transparent 50%),
    radial-gradient(circle at 75% 75%, rgba(255, 255, 255, 0.1) 0%, transparent 50%);
  pointer-events: none;
}

.login-card {
  width: 100%;
  max-width: 450px;
  margin: 20px;
  border-radius: 16px;
  backdrop-filter: blur(10px);
  background: rgba(255, 255, 255, 0.95);
}

.login-header {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 16px 16px 0 0;
}

/* Responsive */
@media (max-width: 600px) {
  .login-card {
    margin: 10px;
    border-radius: 12px;
  }
  .login-header {
    padding: 24px !important;
  }
  .login-card .v-card-text {
    padding: 24px !important;
  }
}

/* Animación */
.login-card {
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Hover */
.v-btn:hover {
  transform: translateY(-1px);
  transition: transform 0.2s ease;
}

.login-card:hover {
  transform: translateY(-2px);
  transition: transform 0.3s ease;
}
</style>
