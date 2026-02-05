<template>
  <v-container :class="containerClass" :fluid="fluid">
    <v-card class="pa-4" elevation="2">
      <!-- Titulo -->
      <v-card-title>
        <slot name="title">
          {{ title }}
        </slot>
        <v-spacer />
        <v-text-field v-model="searchModel" clearable hide-details label="Buscar" prepend-icon="mdi-magnify" />
      </v-card-title>

      <!-- Elementos en Top -->
      <slot name="top" />

      <!-- Tabla -->
      <v-data-table-virtual
class="elevation-1" fixed-header :headers="headers" :items="items" :search="searchModel"
        v-bind="$attrs">
        <!-- Stock -->
        <template #[`item.stock`]="{ item }">
          <slot :item="item" name="item.stock">
            <span>{{ item.stock }}</span>
          </slot>
        </template>

        <!-- Available -->
        <template #[`item.available`]="{ item }">
          <slot :item="item" name="item.available">
            <v-chip :color="item.available ? availableTrueColor : availableFalseColor">
              {{ item.available ? availableTrueText : availableFalseText }}
            </v-chip>
          </slot>
        </template>

        <!-- Acciones -->
        <template #[`item.actions`]="{ item }">
          <slot :item="item" name="item.actions" />
        </template>

        <!-- Slot genérico para TODAS las demás columnas -->
        <template v-for="header in headers">
          <slot :item="header" :name="header.key">
            {{ header.key }}
          </slot>
        </template>
      </v-data-table-virtual>

      <!-- Elementos en Bottom -->
      <slot name="bottom" />
    </v-card>
  </v-container>
</template>

<script setup lang="ts">
import type { ItemTableProps } from '@/types/table.types'

const searchModel = defineModel<string>('search', { default: '' })

withDefaults(defineProps<ItemTableProps>(), {
  fluid: true,
  containerClass: '',
  elevation: 2,
  title: '',
  availableTrueText: 'Disponible',
  availableFalseText: 'No disponible',
  availableTrueColor: 'success',
  availableFalseColor: 'error',
})
</script>
