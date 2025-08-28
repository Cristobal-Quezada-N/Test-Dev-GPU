<template>
  <div class="login-container">
    <!-- Background Pattern -->
    <div class="background-pattern" />

    <!-- Login Card -->
    <v-card class="login-card elevation-12">
      <!-- Header -->
      <div class="login-header text-center pa-6">
        <v-avatar class="mb-4" color="primary" size="80">
          <v-icon color="white" size="40">
            mdi-handshake
          </v-icon>
        </v-avatar>
        <h1 class="text-h4 font-weight-bold text-primary mb-2">
          Prestamos CEII
        </h1>
        <p class="text-body-1 text-medium-emphasis">
          Sistema de Gestión de Préstamos
        </p>
      </div>

      <v-divider />

      <!-- Demo Credentials Info -->
      <v-alert
        class="ma-4"
        icon="mdi-information"
        type="info"
        variant="tonal"
      >
        <div class="text-body-2">
          <strong>Credenciales de Demostración:</strong><br>
          <strong>Admin:</strong> admin@demo.com / admin123<br>
          <strong>Usuario:</strong> user@demo.com / user123<br>
          <strong>Juan:</strong> juan@demo.com / juan123
        </div>
      </v-alert>

      <!-- Login Form -->
      <v-card-text class="pa-6">
        <v-form @submit.prevent="handleSubmit">
          <!-- Email Field -->
          <v-text-field
            v-model="authStore.loginForm.email"
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
            v-model="authStore.loginForm.password"
            :append-inner-icon="authStore.showPassword ? 'mdi-eye-off' : 'mdi-eye'"
            autocomplete="current-password"
            class="mb-6"
            label="Contraseña"
            prepend-inner-icon="mdi-lock"
            required
            :rules="authStore.passwordRules"
            :type="authStore.showPassword ? 'text' : 'password'"
            variant="outlined"
            @click:append-inner="authStore.showPassword = !authStore.showPassword"
          />

          <!-- Remember Me & Forgot Password -->
          <div class="d-flex justify-space-between align-center mb-6">
            <v-checkbox
              v-model="authStore.loginForm.rememberMe"
              color="primary"
              hide-details
              label="Recordarme"
            />
            <v-btn
              color="primary"
              size="small"
              variant="text"
              @click="authStore.forgotPassword"
            >
              ¿Olvidaste tu contraseña?
            </v-btn>
          </div>

          <!-- Login Button -->
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
              mdi-login
            </v-icon>
            Iniciar Sesión
          </v-btn>

          <!-- Demo Login Buttons -->
          <div class="demo-login-section">
            <v-divider class="mb-4">
              <span class="text-caption text-medium-emphasis">O usar cuenta de demostración</span>
            </v-divider>

            <v-row>
              <v-col cols="6">
                <v-btn
                  block
                  color="primary"
                  :loading="authStore.demoLoading === 'user'"
                  size="small"
                  variant="outlined"
                  @click="authStore.demoLogin('user')"
                >
                  <v-icon left size="small">
                    mdi-account
                  </v-icon>
                  Usuario
                </v-btn>
              </v-col>
              <v-col cols="6">
                <v-btn
                  block
                  color="secondary"
                  :loading="authStore.demoLoading === 'admin'"
                  size="small"
                  variant="outlined"
                  @click="authStore.demoLogin('admin')"
                >
                  <v-icon left size="small">
                    mdi-shield-account
                  </v-icon>
                  Admin
                </v-btn>
              </v-col>
            </v-row>
          </div>
        </v-form>
      </v-card-text>

      <!-- Footer -->
      <v-card-actions class="pa-6 pt-0">
        <div class="text-center w-100">
          <p class="text-caption text-medium-emphasis">
            ¿No tienes una cuenta?
            <!-- Para #MOCK  plicar en v-btn -->
            <v-btn
              color="primary"
              size="small"
              variant="text"
              @click="authStore.register"
            >
              <!-- <v-btn -->
              <!--   color="primary" -->
              <!--   size="small" -->
              <!--   variant="text" -->
              <!--   @click="$router.push('/register')" -->
              <!-- > -->
              Regístrate aquí
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
  import { useLogin } from '@/services/auth/auth.mutations'
  import { useAuthStore } from '@/stores/auth'
  import { useNotificationStore } from '@/stores/notification'

  // Define page meta to use auth layout
  definePage({
    meta: {
      layout: 'auth',
      requiresAuth: false,
    },
  })

  const authStore = useAuthStore()
  const loginForm = authStore.loginForm
  const router = useRouter()
  const { mutate: login } = useLogin()
  const notificationStore = useNotificationStore()

  // Handle form submission with validation
  const handleSubmit = async () => {
    console.log('handleSubmit', loginForm)
    try {
      login(loginForm, {
        onSuccess: () => {
          router.push('/')
        },
        onError: error => {
          notificationStore.notify(error.message, 'error')
        },
      })
    } catch (error) {
      console.error('Error submitting form:', error)
    }
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

.demo-login-section {
  margin-top: 20px;
}

/* Responsive adjustments */
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

/* Custom animations */
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

/* Hover effects */
.v-btn:hover {
  transform: translateY(-1px);
  transition: transform 0.2s ease;
}

.login-card:hover {
  transform: translateY(-2px);
  transition: transform 0.3s ease;
}
</style>
