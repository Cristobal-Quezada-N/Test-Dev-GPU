<template>
  <!-- Tabla de Préstamos -->
  <ItemsTable
    :headers="headers"
    :items="loans"
    :search="search"
    title="Préstamos"
  >
    <!-- Slot para personalizar la columna de Estado -->
    <template #item.statusName="{ item }">
      <v-chip :color="getStatusColor(item.statusId)" dark>
        {{ statusMap[item.statusId] || 'Desconocido' }}
      </v-chip>
    </template>
  </ItemsTable>
</template>

<script setup lang="ts">
import axios from 'axios'
import { ref, onMounted } from 'vue'
import ItemsTable from '@/components/ItemsTable.vue'

interface Loan {
  id: number
  userId: number
  itemId: number
  statusId: number
}

interface User { id: number; name: string }
interface Item { id: number; name: string }

const statusMap: Record<number, string> = {
  1: 'Recibido',
  2: 'Aceptado',
  3: 'Denegado',
}

const statusColors: Record<number, string> = {
  1: 'orange',
  2: 'green',
  3: 'red',
}

const getStatusColor = (id: number) => statusColors[id] || 'grey'

const headers = [
  { title: 'ID', key: 'id', align: 'start' },
  { title: 'Usuario', key: 'userName', align: 'start' },
  { title: 'Ítem', key: 'itemName', align: 'start' },
  { title: 'Estado', key: 'statusName', align: 'center' },
]

const search = ref('')
const loans = ref<any[]>([])
const users = ref<User[]>([])
const items = ref<Item[]>([])

const fetchLoans = async () => {
  try {
    const token = localStorage.getItem('auth_token')
    const [loansRes, usersRes, itemsRes] = await Promise.all([
      axios.get<Loan[]>('http://localhost:8090/api/loans/getLoans', {
        headers: { Authorization: `Bearer ${token}` },
      }),
      axios.get<User[]>('http://localhost:8090/api/users/getUsers', {
        headers: { Authorization: `Bearer ${token}` },
      }),
      axios.get<Item[]>('http://localhost:8090/api/items/getItems', {
        headers: { Authorization: `Bearer ${token}` },
      }),
    ])

    users.value = usersRes.data
    items.value = itemsRes.data

    // Aquí agregamos userName, itemName y statusName
    loans.value = loansRes.data.map(loan => ({
      ...loan,
      userName: getUserName(loan.userId),
      itemName: getItemName(loan.itemId),
      statusName: statusMap[loan.statusId] || 'Desconocido',
    }))
  } catch (error) {
    console.error('Error al obtener datos:', error)
  }
}

const getUserName = (id: number) =>
  users.value.find(u => u.id === id)?.email || 'Desconocido'

const getItemName = (id: number) =>
  items.value.find(i => i.id === id)?.name || 'Desconocido'

onMounted(fetchLoans)
</script>
