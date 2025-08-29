<template>
  <div>
    <!-- Mensaje de Bienvenida -->
    <div class="mb-6">
      <h1 class="text-h4 font-weight-bold text-primary mb-2">
        ¡Bienvenido al sistema de gestión de préstamos!
      </h1>
      <p class="text-body-1 text-medium-emphasis">
        Selecciona una acción rápida para comenzar.
      </p>
    </div>

    <!-- Quick Actions -->
    <v-row class="mb-6">
      <v-col cols="12">
        <v-card elevation="2">
          <v-card-title class="text-h6 font-weight-bold">
            Acciones Rápidas
          </v-card-title>
          <v-card-text>
            <v-row>
              <v-col cols="12" md="3" sm="6">
                <v-btn
                  block
                  class="mb-2"
                  color="primary"
                  prepend-icon="mdi-file-document-plus"
                  variant="outlined"
                  @click="navigateTo('/solicitar')"
                >
                  Solicitar Préstamo
                </v-btn>
              </v-col>
              <v-col cols="12" md="3" sm="6">
                <v-btn
                  block
                  class="mb-2"
                  color="secondary"
                  prepend-icon="mdi-history"
                  variant="outlined"
                  @click="navigateTo('/my-loans')"
                >
                  Mis Préstamos
                </v-btn>
              </v-col>
              <v-col cols="12" md="3" sm="6">
                <v-btn
                  block
                  class="mb-2"
                  color="info"
                  prepend-icon="mdi-package-variant"
                  variant="outlined"
                  @click="navigateTo('/inventario')"
                >
                  Ver Inventario
                </v-btn>
              </v-col>
              <v-col cols="12" md="3" sm="6">
                <v-btn
                  block
                  class="mb-2"
                  color="warning"
                  prepend-icon="mdi-file-document"
                  variant="outlined"
                  @click="navigateTo('/requests')"
                >
                  Ver Solicitudes
                </v-btn>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const navigateTo = (path: string) => {
  router.push(path)
}

</script>