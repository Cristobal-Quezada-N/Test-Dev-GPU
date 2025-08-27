<template>
  <v-container>
    <v-card class="pa-4" elevation="2">
      <v-card-title>
        Inventario de Juegos
        <v-spacer />
        <v-text-field
          v-model="search"
          clearable
          hide-details
          label="Buscar"
          prepend-icon="mdi-magnify"
        />
      </v-card-title>

      <!-- Tabla de juegos -->
      <v-data-table-virtual
        class="elevation-1"
        fixed-header
        :headers="headers"
        :items="items"
        :search="search"
      >
        <!-- stock -->
        <template #item.stock="{ item }">
          <span>{{ item.stock }}</span>
        </template>

        <!-- estado disponible -->
        <template #item.available="{ item }">
          <v-chip :color="item.available ? 'green' : 'red'" dark>
            {{ item.available ? "Disponible" : "No disponible" }}
          </v-chip>
        </template>

        <!-- acciones -->
        <template #item.actions="{ item }">
          <v-btn color="blue" icon @click="openDialog(item)">
            <v-icon>mdi-pencil</v-icon>
          </v-btn>
          <v-divider class="mx-2" vertical />
          <v-btn color="red" icon @click="confirmDelete(item)">
            <v-icon>mdi-delete</v-icon>
          </v-btn>
        </template>
      </v-data-table-virtual>

      <!-- boton agregar -->
      <div class="d-flex justify-end mt-4">
        <v-chip
          color="primary"
          pill
          text-color="white"
          @click="openDialog()"
        >
          <v-icon start>
            mdi-plus
          </v-icon>
          Agregar Juego
        </v-chip>
      </div>
    </v-card>

    <!-- dialogo de edición -->
    <ItemDialog v-model="dialog" :item="selectedItem" @save="saveItem" />

    <!-- dialogo confirmación borrado -->
    <v-dialog v-model="confirmDialog" max-width="400px">
      <v-card>
        <v-card-title class="headline">
          Confirmar eliminación
        </v-card-title>
        <v-card-text>
          ¿Estás seguro que deseas eliminar el juego
          <strong>{{ itemToDelete?.name }}</strong>?
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="grey" text @click="confirmDialog = false">
            Cancelar
          </v-btn>
          <v-btn color="red" text @click="deleteConfirmed">
            Eliminar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup lang="ts">
  import axios from 'axios'
  import { onMounted, ref } from 'vue'
  import ItemDialog from '../components/ItemDialog.vue'

  interface Item {
    id: number
    name: string
    stock: number
    available: boolean
  }

  const search = ref('')
  const dialog = ref(false)
  const selectedItem = ref<Item | undefined>(undefined)

  const confirmDialog = ref(false)
  const itemToDelete = ref<Item | null>(null)

  const headers = [
    { title: 'ID', key: 'id', align: 'start' },
    { title: 'Nombre', key: 'name', align: 'start' },
    { title: 'Stock', key: 'stock', align: 'end' },
    { title: 'Estado', key: 'available', align: 'center' },
    { title: 'Acciones', key: 'actions', align: 'center', sortable: false },
  ]

  const items = ref<Item[]>([])

  const fetchItems = async () => {
    try {
      const response = await axios.get<Item[]>(
        'http://localhost:8090/api/items/getItems',
      )
      items.value = response.data
    } catch (error) {
      console.error('Error al obtener el inventario:', error)
    }
  }

  const openDialog = (item?: Item) => {
    selectedItem.value = item ? { ...item } : undefined
    dialog.value = true
  }

  const confirmDelete = (item: Item) => {
    itemToDelete.value = item
    confirmDialog.value = true
  }

  const deleteConfirmed = async () => {
    if (!itemToDelete.value) return
    try {
      await axios.delete(
        `http://localhost:8090/api/items/${itemToDelete.value.id}`,
      )
      items.value = items.value.filter(item => item.id !== itemToDelete.value?.id)
    } catch (error) {
      console.error('Error al eliminar el juego:', error)
    } finally {
      confirmDialog.value = false
      itemToDelete.value = null
    }
  }

  const saveItem = async (item: Item) => {
    try {
      if (item.id) {
        const response = await axios.put(
          `http://localhost:8090/api/items/updateItem/${item.id}`,
          item,
        )
        const index = items.value.findIndex(i => i.id === item.id)
        if (index !== -1) items.value[index] = response.data
      } else {
        const response = await axios.post(
          'http://localhost:8090/api/items/createItem',
          item,
        )
        items.value.push(response.data)
      }
    } catch (error) {
      console.error('Error al guardar el juego:', error)
    }
  }

  onMounted(fetchItems)
</script>
