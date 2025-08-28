<template>
  <!-- Tabla de Items -->
  <ItemsTable
    :headers="headers"
    :items="items"
    :search="search"
    title="Prestamos"
  />
</template>

<script setup lang="ts">
  import ItemsTable from '@/components/ItemsTable.vue'
  const headers = [
    { title: 'ID', key: 'id', align: 'start' },
    { title: 'Nombre', key: 'name', align: 'start' },
    { title: 'Estado', key: 'available', align: 'center' },
  ]
</script>

<style scoped>
</style>