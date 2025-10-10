<template>
  <v-container v-if ="accesoPermitido">
  <!-- Tabla de Usuarios -->
  <ItemsTable
    :headers="headers"
    :items="users"
    :search="search"
    title="Usuarios"
  >
    <!-- Rol como Chip -->
    <template #item.roleId="{ item }">
      <v-chip
        class="ma-1"
        :color="getRoleColor(item.roleId)"
        variant="tonal"
        label
      >
        {{ getRoleLabel(item.roleId) }}
      </v-chip>
    </template>

    <!-- Estado como Chip -->
    <template #item.statusId="{ item }">
      <v-chip
        class="ma-1"
        :color="getStatusColor(item.statusId)"
        variant="tonal"
        label
      >
        {{ getStatusLabel(item.statusId) }}
      </v-chip>
    </template>

    <!-- Acciones -->
    <template #item.actions="{ item }">
      <v-btn color="blue" @click="viewUser(item)">Ver</v-btn>
      <v-divider class="mx-2" vertical />
      <v-btn color="red" @click="confirmDelete(item)">Eliminar</v-btn>
    </template>
  </ItemsTable>

  <!-- Diálogo Confirmación Eliminar -->
  <v-dialog v-model="showDeleteDialog" max-width="400">
    <v-card>
      <v-card-title class="text-h6">Confirmar eliminación</v-card-title>
      <v-card-text>
        ¿Estás seguro que deseas eliminar al usuario
        <strong>{{ selectedUser?.email }}</strong>?
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn variant="tonal" color="grey" @click="showDeleteDialog = false">
          Cancelar
        </v-btn>
        <v-btn variant="tonal" color="red" @click="deleteUser">
          Eliminar
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- Diálogo Ver/Editar Usuario -->
  <v-dialog v-model="showViewDialog" max-width="500">
    <v-card>
      <v-card-title class="text-h6">Información del Usuario</v-card-title>
      <v-card-text>
        <p><strong>Correo:</strong> {{ selectedUser?.email }}</p>
        <p><strong>Nombre:</strong> {{ selectedUser?.name }}</p>

        <p>
          <strong>Rol:</strong>
          <v-chip
            class="ma-1"
            :color="getRoleColor(selectedUser?.roleId)"
            variant="tonal"
            label
          >
            {{ getRoleLabel(selectedUser?.roleId) }}
          </v-chip>
        </p>

        <p>
          <strong>Estado:</strong>
          <v-select
            v-model="editableStatus"
            :items="statusOptions"
            item-title="label"
            item-value="id"
            density="compact"
            variant="outlined"
          >
            <template #selection="{ item }">
              <v-chip
                class="ma-1"
                :color="getStatusColor(item.value)"
                variant="tonal"
                label
              >
                {{ item.title }}
              </v-chip>
            </template>
            <template #item="{ props, item }">
              <v-list-item v-bind="props">
                <v-chip
                  class="ma-1"
                  :color="getStatusColor(item.value)"
                  variant="tonal"
                  label
                >
                  {{ item.title }}
                </v-chip>
              </v-list-item>
            </template>
          </v-select>
        </p>
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn variant="tonal" color="grey" @click="showViewDialog = false">
          Cerrar
        </v-btn>
        <v-btn variant="tonal" color="green" @click="updateStatus">
          Guardar cambios
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
  </v-container>
</template>

<script setup lang="ts">
import ItemsTable from '@/components/ItemsTable.vue'
import { ref, onMounted } from 'vue'
import axios from 'axios'

const headers = [
  { title: 'Correo', key: 'email', align: 'start' },
  { title: 'Rol', key: 'roleId', align: 'center' },
  { title: 'Estado', key: 'statusId', align: 'center' },
  { title: 'Acciones', key: 'actions', align: 'center', sortable: false },
]

const users = ref<any[]>([])
const search = ref('')
const showDeleteDialog = ref(false)
const showViewDialog = ref(false)
const selectedUser = ref<any | null>(null)
const editableStatus = ref<number | null>(null)
const accesoPermitido = ref(false);

const statusOptions = [
  { id: 1, label: 'Recibido' },
  { id: 2, label: 'Aceptado' },
  { id: 3, label: 'Denegado' },
]

onMounted(() => {
  const userRole = JSON.parse(localStorage.getItem('auth_user') || '{}');
  if (userRole.roleId  === 1) {
    accesoPermitido.value = true;
    fetchUsers();
  } else {
    accesoPermitido.value = false;
    alert("No tienes permiso para acceder a esta página.");
  }
});

// Llamar API
const fetchUsers = async () => {
  try {
    const token = localStorage.getItem('auth_token')
    const res = await axios.get('http://localhost:8090/api/users/getUsers', {
      headers: { Authorization: `Bearer ${token}` },
    })
    users.value = res.data
  } catch (error) {
    console.error('Error al obtener usuarios:', error)
  }
}

// Funciones para roles
const getRoleLabel = (roleId: number) => {
  switch (roleId) {
    case 1:
      return 'Administrador'
    case 2:
      return 'Usuario'
    case 3:
      return 'Invitado'
    default:
      return 'Desconocido'
  }
}

const getRoleColor = (roleId: number) => {
  switch (roleId) {
    case 1:
      return 'red'
    case 2:
      return 'green'
    default:
      return 'grey'
  }
}

// Funciones para estado
const getStatusLabel = (statusId: number) => {
  switch (statusId) {
    case 1:
      return 'Recibido'
    case 2:
      return 'Aceptado'
    case 3:
      return 'Denegado'
    default:
      return 'Desconocido'
  }
}

const getStatusColor = (statusId: number) => {
  switch (statusId) {
    case 1:
      return 'yellow'
    case 2:
      return 'green'
    case 3:
      return 'red'
    default:
      return 'grey'
  }
}

// Abrir diálogo de confirmación eliminar
const confirmDelete = (user: any) => {
  selectedUser.value = user
  showDeleteDialog.value = true
}

// Eliminar usuario
const deleteUser = async () => {
  if (!selectedUser.value) return
  try {
    const token = localStorage.getItem('auth_token')
    await axios.delete(
      `http://localhost:8090/api/users/deleteUser/${selectedUser.value.id}`,
      {
        headers: { Authorization: `Bearer ${token}` },
      }
    )
    fetchUsers()
  } catch (error) {
    console.error('Error al eliminar usuario:', error)
  } finally {
    showDeleteDialog.value = false
    selectedUser.value = null
  }
}

// Ver usuario
const viewUser = (user: any) => {
  selectedUser.value = user
  editableStatus.value = user.statusId
  showViewDialog.value = true
}

// Actualizar estado
const updateStatus = async () => {
  if (!selectedUser.value) return
  try {
    const token = localStorage.getItem('auth_token')
    await axios.put(
      `http://localhost:8090/api/users/updateStatus/${selectedUser.value.id}`,
      { statusId: editableStatus.value },
      { headers: { Authorization: `Bearer ${token}` } }
    )
    fetchUsers()
  } catch (error) {
    console.error('Error al actualizar estado:', error)
  } finally {
    showViewDialog.value = false
  }
}

onMounted(fetchUsers)
</script>
