<template>
  <div>
    <!-- Dashboard Header -->
    <div class="d-flex align-center justify-space-between mb-6">
      <div>
        <h1 class="text-h4 font-weight-bold text-primary mb-2">
          Dashboard
        </h1>
        <p class="text-body-1 text-medium-emphasis">
          Bienvenido al sistema de gestión de préstamos
        </p>
      </div>
      <v-btn
        color="primary"
        prepend-icon="mdi-plus"
        @click="createNewLoan"
      >
        Nuevo Préstamo
      </v-btn>
    </div>

    <!-- Statistics Cards -->
    <v-row class="mb-6">
      <v-col cols="12" md="3" sm="6">
        <v-card class="pa-4" elevation="2">
          <div class="d-flex align-center">
            <v-avatar
              class="mr-4"
              color="primary"
              size="48"
            >
              <v-icon color="white" size="24">
                mdi-package-variant
              </v-icon>
            </v-avatar>
            <div>
              <div class="text-h5 font-weight-bold text-primary">
                {{ stats.totalItems }}
              </div>
              <div class="text-body-2 text-medium-emphasis">
                Total de Items
              </div>
            </div>
          </div>
        </v-card>
      </v-col>

      <v-col cols="12" md="3" sm="6">
        <v-card class="pa-4" elevation="2">
          <div class="d-flex align-center">
            <v-avatar
              class="mr-4"
              color="success"
              size="48"
            >
              <v-icon color="white" size="24">
                mdi-check-circle
              </v-icon>
            </v-avatar>
            <div>
              <div class="text-h5 font-weight-bold text-success">
                {{ stats.activeLoans }}
              </div>
              <div class="text-body-2 text-medium-emphasis">
                Préstamos Activos
              </div>
            </div>
          </div>
        </v-card>
      </v-col>

      <v-col cols="12" md="3" sm="6">
        <v-card class="pa-4" elevation="2">
          <div class="d-flex align-center">
            <v-avatar
              class="mr-4"
              color="warning"
              size="48"
            >
              <v-icon color="white" size="24">
                mdi-clock
              </v-icon>
            </v-avatar>
            <div>
              <div class="text-h5 font-weight-bold text-warning">
                {{ stats.pendingRequests }}
              </div>
              <div class="text-body-2 text-medium-emphasis">
                Solicitudes Pendientes
              </div>
            </div>
          </div>
        </v-card>
      </v-col>

      <v-col cols="12" md="3" sm="6">
        <v-card class="pa-4" elevation="2">
          <div class="d-flex align-center">
            <v-avatar
              class="mr-4"
              color="info"
              size="48"
            >
              <v-icon color="white" size="24">
                mdi-account-group
              </v-icon>
            </v-avatar>
            <div>
              <div class="text-h5 font-weight-bold text-info">
                {{ stats.totalUsers }}
              </div>
              <div class="text-body-2 text-medium-emphasis">
                Usuarios Registrados
              </div>
            </div>
          </div>
        </v-card>
      </v-col>
    </v-row>

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
                  @click="navigateTo('/items')"
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

    <!-- Recent Activity & Popular Items -->
    <v-row>
      <v-col cols="12" lg="8">
        <v-card elevation="2">
          <v-card-title class="text-h6 font-weight-bold">
            Actividad Reciente
          </v-card-title>
          <v-card-text>
            <v-list>
              <v-list-item
                v-for="activity in recentActivity"
                :key="activity.id"
                class="mb-2"
                :prepend-avatar="activity.avatar"
                :subtitle="activity.description"
                :title="activity.title"
              >
                <template #append>
                  <v-chip
                    :color="activity.status === 'completed' ? 'success' : 'warning'"
                    size="small"
                  >
                    {{ activity.status === 'completed' ? 'Completado' : 'Pendiente' }}
                  </v-chip>
                </template>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" lg="4">
        <v-card elevation="2">
          <v-card-title class="text-h6 font-weight-bold">
            Items Populares
          </v-card-title>
          <v-card-text>
            <v-list>
              <v-list-item
                v-for="item in popularItems"
                :key="item.id"
                class="mb-2"
                :prepend-avatar="item.image"
                :subtitle="`${item.loanCount} préstamos`"
                :title="item.name"
              >
                <template #append>
                  <v-chip
                    :color="item.available ? 'success' : 'error'"
                    size="small"
                  >
                    {{ item.available ? 'Disponible' : 'Ocupado' }}
                  </v-chip>
                </template>
              </v-list-item>
            </v-list>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </div>
</template>

<script setup lang="ts">
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'

  const router = useRouter()

  // Mock data
  const stats = ref({
    totalItems: 156,
    activeLoans: 23,
    pendingRequests: 8,
    totalUsers: 45,
  })

  const recentActivity = ref([
    {
      id: 1,
      title: 'Préstamo de Laptop',
      description: 'Juan Pérez solicitó una laptop Dell XPS 13',
      status: 'completed',
      avatar: 'https://cdn.vuetifyjs.com/images/lists/1.jpg',
    },
    {
      id: 2,
      title: 'Devolución de Proyector',
      description: 'María García devolvió el proyector Epson',
      status: 'completed',
      avatar: 'https://cdn.vuetifyjs.com/images/lists/2.jpg',
    },
    {
      id: 3,
      title: 'Solicitud de Cámara',
      description: 'Carlos López solicitó una cámara Canon',
      status: 'pending',
      avatar: 'https://cdn.vuetifyjs.com/images/lists/3.jpg',
    },
  ])

  const popularItems = ref([
    {
      id: 1,
      name: 'Laptop Dell XPS 13',
      loanCount: 15,
      available: true,
      image: 'https://cdn.vuetifyjs.com/images/cards/docks.jpg',
    },
    {
      id: 2,
      name: 'Proyector Epson',
      loanCount: 12,
      available: false,
      image: 'https://cdn.vuetifyjs.com/images/cards/house.jpg',
    },
    {
      id: 3,
      name: 'Cámara Canon EOS',
      loanCount: 8,
      available: true,
      image: 'https://cdn.vuetifyjs.com/images/cards/road.jpg',
    },
  ])

  // Methods
  const createNewLoan = () => {
    router.push('/solicitar')
  }

  const navigateTo = (path: string) => {
    router.push(path)
  }
</script>

<style scoped>
.border-l-4 {
  border-left-width: 4px;
  border-left-style: solid;
}

.border-l-primary {
  border-left-color: rgb(var(--v-theme-primary));
}

.border-l-secondary {
  border-left-color: rgb(var(--v-theme-secondary));
}

.border-l-info {
  border-left-color: rgb(var(--v-theme-info));
}

.border-l-success {
  border-left-color: rgb(var(--v-theme-success));
}
</style>
