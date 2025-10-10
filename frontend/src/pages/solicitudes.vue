<template>
  <v-container v-if ="accesoPermitido">
  <!-- Tabla de Solicitudes -->
  <ItemsTable
    :headers="headers"
    :items="loans"
    :search="search"
    title="Solicitudes"
  >
    <!-- Fecha del pedido -->
    <template #item.date="{ item }">
      {{ formatDate(item.date) }}
    </template>

    <!-- Fecha de entrega -->
    <template #item.deadline="{ item }">
      {{ formatDate(item.deadline) }}
    </template>

    <!-- Acciones -->
    <template #item.actions="{ item }">
      <v-btn color="green" @click="acceptLoan(item.id)">Aceptar</v-btn>
      <v-divider class="mx-2" vertical />
      <v-btn color="red" @click="rejectLoan(item.id)">Rechazar</v-btn>
    </template>
  </ItemsTable>
  </v-container>
</template>

<script setup lang="ts">
import ItemsTable from '@/components/ItemsTable.vue'
import { ref, onMounted } from 'vue'
import axios from 'axios'

interface Loan {
  id: number
  userId: number
  date: string
  deadline: string
  statusId: number
}

interface User {
  id: number
  email: string
}

const headers = [
  { title: 'ID', key: 'id', align: 'start' },
  { title: 'Usuario', key: 'userEmail', align: 'start' },
  { title: 'Fecha del Pedido', key: 'date', align: 'center' },
  { title: 'Fecha de Entrega', key: 'deadline', align: 'center' },
  { title: 'Acciones', key: 'actions', align: 'center', sortable: false },
]

const loans = ref<any[]>([])
const users = ref<User[]>([])
const search = ref('')
const accesoPermitido = ref(false);

onMounted(() => {
  const userRole = JSON.parse(localStorage.getItem('auth_user') || '{}');
  if (userRole.roleId === 1) {
    accesoPermitido.value = true;
    fetchLoans();
  } else {
    accesoPermitido.value = false;
    alert("No tienes permiso para acceder a esta página.");
  }
});

const fetchLoans = async () => {
  try {
    const token = localStorage.getItem('auth_token')

    const [loansRes, usersRes] = await Promise.all([
      axios.get<Loan[]>('http://localhost:8090/api/loans/getLoans', {
        headers: { Authorization: `Bearer ${token}` },
      }),
      axios.get<User[]>('http://localhost:8090/api/users/getUsers', {
        headers: { Authorization: `Bearer ${token}` },
      }),
    ])

    users.value = usersRes.data

    // Mapear loans con email
    loans.value = loansRes.data
      .filter((loan: Loan) => loan.statusId === 1)
      .map(loan => ({
        ...loan,
        userEmail: getUserEmail(loan.userId),
      }))
  } catch (error) {
    console.error('Error al obtener préstamos:', error)
  }
}

const getUserEmail = (id: number) =>
  users.value.find(u => u.id === id)?.email || 'Desconocido'

const acceptLoan = async (id: number) => {
  const token = localStorage.getItem('auth_token')
  await axios.patch(`http://localhost:8090/api/loans/acceptLoans/${id}`, {}, {
    headers: { Authorization: `Bearer ${token}` },
  })
  fetchLoans()
}

const rejectLoan = async (id: number) => {
  const token = localStorage.getItem('auth_token')
  await axios.patch(`http://localhost:8090/api/loans/rejectLoans/${id}`, {}, {
    headers: { Authorization: `Bearer ${token}` },
  })
  fetchLoans()
}

// Función para formatear fecha y hora
const formatDate = (isoString: string) => {
  if (!isoString) return ''
  const date = new Date(isoString)
  return date.toLocaleString('es-CL', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

onMounted(fetchLoans)
</script>
