<template>
  <v-card class="elevation-2">
    <v-card-title class="text-h6 font-weight-bold">
      Acciones Rápidas
    </v-card-title>
    <v-card-text>
      <v-row>
        <v-col cols="12" sm="6" md="3">
          <v-btn
            block
            variant="outlined"
            color="primary"
            prepend-icon="mdi-plus"
            class="mb-2"
            @click="$router.push('/items')"
          >
            Buscar en Inventario
          </v-btn>
        </v-col>
        <v-col cols="12" sm="6" md="3">
          <v-btn
            block
            variant="outlined"
            color="secondary"
            prepend-icon="mdi-file-document-plus"
            class="mb-2"
            @click="$router.push('/requests')"
          >
            Buscar Solicitudes
          </v-btn>
        </v-col>
        <v-col cols="12" sm="6" md="3">
          <v-btn
            block
            variant="outlined"
            color="info"
            prepend-icon="mdi-history"
            class="mb-2"
            @click="$router.push('/my-loans')"
          >
            My Loans
          </v-btn>
        </v-col>
        <v-col cols="12" sm="6" md="3">
          <v-btn
            v-if="appStore.isAdmin"
            block
            variant="outlined"
            color="accent"
            prepend-icon="mdi-chart-line"
            class="mb-2"
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