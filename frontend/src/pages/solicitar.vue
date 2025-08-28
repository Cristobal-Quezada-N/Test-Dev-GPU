<template>
  <v-container>
    <v-card class="pa-4" elevation="2">
      <div class="text-caption mb-2" style="color: gray;">
        Fecha local: {{ fechaLocal }}
      </div>
      <v-card-title>
        Solicitudes de Préstamos
        <v-spacer />
        <v-text-field
          v-model="search"
          clearable
          hide-details
          label="Buscar"
          prepend-icon="mdi-magnify"
        />
      </v-card-title>

      <!-- Tabla de Items -->
      <ItemsTable
        :headers="headers"
        :items="items"
        :search="search"
      >
        <template #item.actions="{ item }">
          <v-btn color="amber-lighten-2" @click="confirmarLoan(item)">
            Solicitar
          </v-btn>
          <v-divider class="mx-2" vertical />
        </template>
      </ItemsTable>
    </v-card>

    <v-dialog v-model="confirmLoan" max-width="400px">
      <v-card>
        <v-card-title class="headline">
          Solicitar préstamo
        </v-card-title>
        <v-card-text>
          <div>
            ¿Cuánto tiempo estimas usar el juego <strong>{{ selectedItem?.name }}</strong>?
          </div>
          <v-text-field
            v-model="tiempoEstimado"
            class="mt-4"
            label="Tiempo estimado (horas)"
            min="1"
            required
            type="number"
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn color="grey" text @click="confirmLoan = false">
            Cancelar
          </v-btn>
          <v-btn color="primary" text @click="solicitarLoan">
            Solicitar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup lang="ts">
  import axios from 'axios'
  import { onMounted, ref } from 'vue'

  import ItemsTable from '@/components/ItemsTable.vue'

  interface Item {
    id: number
    name: string
    stock: number
    available: boolean
  }

  const fechaLocal = ref(new Date().toLocaleString())
  const search = ref('')
  const selectedItem = ref<Item | undefined>(undefined)
  const confirmLoan = ref(false)

  const headers = [
    { title: 'ID', key: 'id', align: 'start' },
    { title: 'Nombre', key: 'name', align: 'start' },
    { title: 'Stock', key: 'stock', align: 'end' },
    { title: 'Estado', key: 'available', align: 'center' },
    { title: 'Acciones', key: 'actions', align: 'center', sortable: false },
  ]

  const items = ref<Item[]>([])

  const tiempoEstimado = ref<number | null>(null)

  const solicitarLoan = async () => {
    if (!selectedItem.value || !tiempoEstimado.value) {
      alert('Por favor ingresa el tiempo estimado.')
      return
    }
    try {
      // Aquí iría la lógica para solicitar el préstamo con el tiempo estimado
      // await axios.post("http://localhost:8090/api/loans/request", { itemId: selectedItem.value.id, tiempo: tiempoEstimado.value });
      alert(`Solicitud enviada para "${selectedItem.value.name}" por ${tiempoEstimado.value} horas.`)
      confirmLoan.value = false
      tiempoEstimado.value = null
    } catch (error) {
      console.error('Error al solicitar el préstamo:', error)
      alert('Error al solicitar el préstamo. Inténtalo de nuevo.')
    }
  }

  const confirmarLoan = (item: Item) => {
    selectedItem.value = item
    confirmLoan.value = true
  }

  const fetchItems = async () => {
    try {
      const response = await axios.get<Item[]>(
        'http://localhost:8090/api/items/getItems',
      )
      items.value = response.data

      console.log('Items fetched:', items.value)
    } catch (error) {
      console.error('Error al obtener el inventario:', error)
    }
  }

  onMounted(() => {
    fetchItems()
    fechaLocal.value = new Date().toLocaleString()
  })
</script>
