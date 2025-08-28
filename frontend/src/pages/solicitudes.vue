<template>
  <!-- Tabla de Items -->
  <ItemsTable
    :headers="headers"
    :items="items"
    :search="search"
    title="Solicitudes"
  >
    <!-- Implementar Acciones -->
    <!-- <template #item.actions="{ item }"> -->
    <!--   <v-btn color="amber-lighten-2" @click="function(item)"> -->
    <!--     accion -->
    <!--   </v-btn> -->
    <!--   <v-divider class="mx-2" vertical /> -->
    <!-- </template> -->
  </ItemsTable>
</template>

<script setup lang="ts">
  import ItemsTable from '@/components/ItemsTable.vue'

  const headers = [
    { title: 'ID', key: 'id', align: 'start' },
    { title: 'Nombre', key: 'name', align: 'start' },
    { title: 'Fecha del Pedido', key: 'available', align: 'center' },
    { title: 'Fecha de Entrega', key: 'available', align: 'center' },
    { title: 'Estado', key: 'available', align: 'center' },
    { title: 'Acciones', key: 'actions', align: 'center', sortable: false },
  ]
</script>

<style scoped>
</style>
