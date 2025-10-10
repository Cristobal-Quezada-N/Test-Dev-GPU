<template>
  <v-card class="elevation-2">
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
            prepend-icon="mdi-plus"
            variant="outlined"
            @click="$router.push('/items')"
          >
            Buscar en Inventario
          </v-btn>
        </v-col>
        <v-col cols="12" md="3" sm="6">
          <v-btn
            block
            class="mb-2"
            color="secondary"
            prepend-icon="mdi-file-document-plus"
            variant="outlined"
            @click="$router.push('/requests')"
          >
            Buscar Solicitudes
          </v-btn>
        </v-col>
        <v-col cols="12" md="3" sm="6">
          <v-btn
            block
            class="mb-2"
            color="info"
            prepend-icon="mdi-history"
            variant="outlined"
            @click="$router.push('/my-loans')"
          >
            My Loans
          </v-btn>
        </v-col>
        <v-col cols="12" md="3" sm="6">
          <v-btn
            v-if="appStore.isAdmin"
            block
            class="mb-2"
            color="accent"
            prepend-icon="mdi-chart-line"
            variant="outlined"
            @click="$router.push('/admin/reports')"
          >
            View Reports
          </v-btn>
        </v-col>
      </v-row>
    </v-card-text>
  </v-card>
</template>

<script setup lang="ts">
  import { useAppStore } from '@/stores/app'

  const appStore = useAppStore()
</script>
