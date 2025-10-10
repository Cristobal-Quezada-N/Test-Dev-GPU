<template>
  <div>
    <!-- Page Header -->
    <div class="mb-6">
      <h1 class="text-h4 font-weight-bold text-primary mb-2">
        User Management
      </h1>
      <p class="text-body-1 text-medium-emphasis">
        Manage system users and their permissions.
      </p>
    </div>

    <!-- Action Bar -->
    <v-card class="elevation-2 mb-6">
      <v-card-text>
        <v-row align="center">
          <v-col cols="12" md="4">
            <v-text-field
              v-model="searchQuery"
              density="compact"
              hide-details
              placeholder="Search users..."
              prepend-inner-icon="mdi-magnify"
              variant="outlined"
            />
          </v-col>
          <v-col cols="12" md="3">
            <v-select
              v-model="selectedRole"
              density="compact"
              hide-details
              :items="roleOptions"
              label="Filter by Role"
              variant="outlined"
            />
          </v-col>
          <v-col cols="12" md="3">
            <v-select
              v-model="selectedStatus"
              density="compact"
              hide-details
              :items="statusOptions"
              label="Filter by Status"
              variant="outlined"
            />
          </v-col>
          <v-col cols="12" md="2">
            <v-btn
              block
              color="primary"
              prepend-icon="mdi-plus"
              @click="openUserDialog()"
            >
              Add User
            </v-btn>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>

    <!-- Users Table -->
    <v-card class="elevation-2">
      <v-card-title class="text-h6 font-weight-bold">
        Users ({{ filteredUsers.length }})
      </v-card-title>
      <v-card-text class="pa-0">
        <v-data-table
          class="elevation-0"
          :headers="headers"
          :items="filteredUsers"
          :loading="loading"
        >
          <template #item.avatar="{ item }">
            <v-avatar color="primary" size="32">
              <span class="text-caption font-weight-bold text-white">
                {{ getUserInitials(item.raw.name) }}
              </span>
            </v-avatar>
          </template>

          <template #item.name="{ item }">
            <div>
              <div class="font-weight-medium">
                {{ item.raw.name }}
              </div>
              <div class="text-caption text-medium-emphasis">
                {{ item.raw.email }}
              </div>
            </div>
          </template>

          <template #item.role="{ item }">
            <v-chip
              :color="item.raw.role === 'admin' ? 'error' : 'primary'"
              size="small"
              variant="tonal"
            >
              {{ item.raw.role }}
            </v-chip>
          </template>

          <template #item.status="{ item }">
            <v-chip
              :color="item.raw.status === 'active' ? 'success' : 'error'"
              size="small"
              variant="tonal"
            >
              {{ item.raw.status }}
            </v-chip>
          </template>

          <template #item.lastLogin="{ item }">
            {{ formatDate(item.raw.lastLogin) }}
          </template>

          <template #item.actions="{ item }">
            <v-btn
              color="primary"
              prepend-icon="mdi-pencil"
              size="small"
              variant="outlined"
              @click="editUser(item.raw)"
            >
              Edit
            </v-btn>
            <v-btn
              class="ml-2"
              color="error"
              prepend-icon="mdi-delete"
              size="small"
              variant="outlined"
              @click="deleteUser(item.raw)"
            >
              Delete
            </v-btn>
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>

    <!-- User Dialog -->
    <v-dialog v-model="userDialog" max-width="600">
      <v-card>
        <v-card-title class="text-h6 font-weight-bold">
          {{ isEditing ? 'Edit User' : 'Add New User' }}
        </v-card-title>
        <v-card-text>
          <v-form ref="userForm">
            <v-row>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="userForm.name"
                  density="compact"
                  label="Full Name"
                  required
                  variant="outlined"
                />
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="userForm.email"
                  density="compact"
                  label="Email"
                  required
                  type="email"
                  variant="outlined"
                />
              </v-col>
            </v-row>
            <v-row>
              <v-col cols="12" md="6">
                <v-select
                  v-model="userForm.role"
                  density="compact"
                  :items="roleOptions"
                  label="Role"
                  required
                  variant="outlined"
                />
              </v-col>
              <v-col cols="12" md="6">
                <v-select
                  v-model="userForm.status"
                  density="compact"
                  :items="statusOptions"
                  label="Status"
                  required
                  variant="outlined"
                />
              </v-col>
            </v-row>
            <v-row v-if="!isEditing">
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="userForm.password"
                  density="compact"
                  label="Password"
                  required
                  type="password"
                  variant="outlined"
                />
              </v-col>
              <v-col cols="12" md="6">
                <v-text-field
                  v-model="userForm.confirmPassword"
                  density="compact"
                  label="Confirm Password"
                  required
                  type="password"
                  variant="outlined"
                />
              </v-col>
            </v-row>
          </v-form>
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn variant="text" @click="userDialog = false">
            Cancel
          </v-btn>
          <v-btn color="primary" :loading="saving" @click="saveUser">
            {{ isEditing ? 'Update' : 'Create' }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- Delete Confirmation Dialog -->
    <v-dialog v-model="deleteDialog" max-width="400">
      <v-card>
        <v-card-title class="text-h6 font-weight-bold">
          Confirm Delete
        </v-card-title>
        <v-card-text>
          Are you sure you want to delete user <strong>{{ selectedUser?.name }}</strong>?
          This action cannot be undone.
        </v-card-text>
        <v-card-actions>
          <v-spacer />
          <v-btn variant="text" @click="deleteDialog = false">
            Cancel
          </v-btn>
          <v-btn color="error" :loading="deleting" @click="confirmDelete">
            Delete
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script lang="ts" setup>
  import { computed, onMounted, ref } from 'vue'
  import { useAppStore } from '@/stores/app'

  const appStore = useAppStore()

  // State
  const loading = ref(false)
  const saving = ref(false)
  const deleting = ref(false)
  const userDialog = ref(false)
  const deleteDialog = ref(false)
  const isEditing = ref(false)
  const selectedUser = ref<any>(null)
  const searchQuery = ref('')
  const selectedRole = ref('all')
  const selectedStatus = ref('all')

  // Form data
  const userForm = ref({
    name: '',
    email: '',
    role: 'user',
    status: 'active',
    password: '',
    confirmPassword: '',
  })

  // Mock data
  const users = ref([
    {
      id: 1,
      name: 'John Doe',
      email: 'john.doe@example.com',
      role: 'admin',
      status: 'active',
      lastLogin: '2024-01-12T10:30:00Z',
    },
    {
      id: 2,
      name: 'Jane Smith',
      email: 'jane.smith@example.com',
      role: 'user',
      status: 'active',
      lastLogin: '2024-01-11T15:45:00Z',
    },
    {
      id: 3,
      name: 'Mike Johnson',
      email: 'mike.johnson@example.com',
      role: 'user',
      status: 'inactive',
      lastLogin: '2024-01-05T09:20:00Z',
    },
    {
      id: 4,
      name: 'Sarah Wilson',
      email: 'sarah.wilson@example.com',
      role: 'user',
      status: 'active',
      lastLogin: '2024-01-12T14:15:00Z',
    },
  ])

  const roleOptions = ref([
    { title: 'All Roles', value: 'all' },
    { title: 'Admin', value: 'admin' },
    { title: 'User', value: 'user' },
  ])

  const statusOptions = ref([
    { title: 'All Status', value: 'all' },
    { title: 'Active', value: 'active' },
    { title: 'Inactive', value: 'inactive' },
  ])

  const headers = [
    { title: 'Avatar', key: 'avatar', sortable: false },
    { title: 'Name', key: 'name', sortable: false },
    { title: 'Role', key: 'role', sortable: false },
    { title: 'Status', key: 'status', sortable: false },
    { title: 'Last Login', key: 'lastLogin', sortable: false },
    { title: 'Actions', key: 'actions', sortable: false },
  ]

  // Computed
  const filteredUsers = computed(() => {
    return users.value.filter(user => {
      const matchesSearch = user.name.toLowerCase().includes(searchQuery.value.toLowerCase())
        || user.email.toLowerCase().includes(searchQuery.value.toLowerCase())
      const matchesRole = selectedRole.value === 'all' || user.role === selectedRole.value
      const matchesStatus = selectedStatus.value === 'all' || user.status === selectedStatus.value

      return matchesSearch && matchesRole && matchesStatus
    })
  })

  // Methods
  const getUserInitials = (name: string) => {
    return name
      .split(' ')
      .map(n => n[0])
      .join('')
      .toUpperCase()
      .slice(0, 2)
  }

  const formatDate = (date: string) => {
    return new Date(date).toLocaleDateString()
  }

  const openUserDialog = (user?: any) => {
    isEditing.value = !!user
    selectedUser.value = user

    userForm.value = user
      ? {
        name: user.name,
        email: user.email,
        role: user.role,
        status: user.status,
        password: '',
        confirmPassword: '',
      }
      : {
        name: '',
        email: '',
        role: 'user',
        status: 'active',
        password: '',
        confirmPassword: '',
      }

    userDialog.value = true
  }

  const editUser = (user: any) => {
    openUserDialog(user)
  }

  const saveUser = async () => {
    saving.value = true

    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1000))

    if (isEditing.value) {
      // Update existing user
      const index = users.value.findIndex(u => u.id === selectedUser.value.id)
      if (index !== -1) {
        users.value[index] = {
          ...users.value[index],
          name: userForm.value.name,
          email: userForm.value.email,
          role: userForm.value.role,
          status: userForm.value.status,
        }
      }
    } else {
      // Create new user
      const newUser = {
        id: users.value.length + 1,
        name: userForm.value.name,
        email: userForm.value.email,
        role: userForm.value.role,
        status: userForm.value.status,
        lastLogin: null,
      }
      users.value.push(newUser)
    }

    userDialog.value = false
    saving.value = false
  }

  const deleteUser = (user: any) => {
    selectedUser.value = user
    deleteDialog.value = true
  }

  const confirmDelete = async () => {
    deleting.value = true

    // Simulate API call
    await new Promise(resolve => setTimeout(resolve, 1000))

    const index = users.value.findIndex(u => u.id === selectedUser.value.id)
    if (index !== -1) {
      users.value.splice(index, 1)
    }

    deleteDialog.value = false
    deleting.value = false
  }

  onMounted(() => {
    loading.value = true
    setTimeout(() => {
      loading.value = false
    }, 500)
  })
</script>

<route lang="yaml">
meta:
  layout: default
  requiresAuth: true
</route>

<style scoped>
</style>
