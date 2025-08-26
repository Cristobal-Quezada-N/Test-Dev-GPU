<template>
  <v-app>
    <!-- Navigation Drawer -->
    <v-navigation-drawer
      v-model="appStore.drawer"
      :rail="rail"
      permanent
      @mouseenter="rail = false"
      @mouseleave="rail = true"
      class="elevation-2"
      
    >
      <!-- Logo Section -->
      <v-list>
          <v-list-item>
            <template v-slot:prepend>
              <v-icon size="32" color="primary">mdi-handshake</v-icon>
            </template>
            <template v-slot:title>
              <div class="text-h6 font-weight-bold text-primary">
                Prestamos CEII
              </div>
            </template>
          </v-list-item>
        </v-list>

      <v-divider class="mb-2"></v-divider>

      <!-- Navigation Menu based on user role -->
      <template v-if="appStore.isAdmin">
        <!-- Admin Navigation -->
        <v-list density="compact" nav>
          <v-list-item
            v-for="item in userNavigationItems"
            :key="item.title"
            :to="item.to"
            :prepend-icon="item.icon"
            :title="item.title"
            :subtitle="item.subtitle"
            rounded="lg"
            :class="{ 'v-list-item--active': $route.path === item.to }"
          >
          </v-list-item>
        </v-list>
      </template>
      
      <template v-else>
        <!-- User Navigation -->
        <v-list density="compact" nav>
          <v-list-item
            v-for="item in userNavigationItems"
            :key="item.title"
            :to="item.to"
            :prepend-icon="item.icon"
            :title="item.title"
            :subtitle="item.subtitle"
            rounded="lg"          
            :class="{ 'v-list-item--active': $route.path === item.to }"
          >
          </v-list-item>
        </v-list>
      </template>

      <!-- Logout Button -->
      <v-spacer></v-spacer>
      <v-list density="compact" nav class="mt-auto">
        <v-list-item
          @click="handleLogout"
          prepend-icon="mdi-logout"
          title="Cerrar Sesión"
          subtitle="Salir del sistema"
          rounded="lg"
          class="mb-2"
        >
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <!-- Top App Bar -->
    <v-app-bar
      elevation="1"
      color="surface"
      class="border-b"
    >
      <v-app-bar-nav-icon
        @click="appStore.toggleDrawer"
        class="d-md-none"
      ></v-app-bar-nav-icon>

      <v-app-bar-title class="text-h6 font-weight-bold text-primary">
        {{ currentPageTitle }}
      </v-app-bar-title>

      <v-spacer></v-spacer>

      <!-- Search Bar -->
      <v-text-field
        v-model="searchQuery"
        prepend-inner-icon="mdi-magnify"
        placeholder="Search items..."
        variant="outlined"
        density="compact"
        hide-details
        class="max-width-300 mr-4"
        style="max-width: 300px"
      ></v-text-field>

      <!-- Notifications -->
      <v-btn
        icon
        variant="text"
        class="mr-2"
      >
        <v-badge
          :content="notificationsCount"
          :model-value="notificationsCount > 0"
          color="error"
        >
          <v-icon>mdi-bell</v-icon>
        </v-badge>
      </v-btn>

      <!-- User Menu -->
      <v-menu
        v-model="userMenu"
        :close-on-content-click="false"
        location="bottom end"
      >
        <template v-slot:activator="{ props }">
          <v-btn
            v-bind="props"
            variant="text"
            class="text-none"
          >
            <v-avatar
              size="32"
              color="primary"
              class="mr-2"
            >
              <span class="text-caption font-weight-bold text-white">
                {{ appStore.userInitials }}
              </span>
            </v-avatar>
            <span v-if="appStore.user" class="text-body-2">
              {{ appStore.user.name }}
            </span>
            <v-icon>mdi-chevron-down</v-icon>
          </v-btn>
        </template>

        <v-card min-width="200" class="elevation-8">
          <v-list>
            <v-list-item>
              <template v-slot:prepend>
                <v-avatar
                  size="40"
                  color="primary"
                >
                  <span class="text-body-1 font-weight-bold text-white">
                    {{ appStore.userInitials }}
                  </span>
                </v-avatar>
              </template>
              <v-list-item-title class="font-weight-medium">
                {{ appStore.user?.name }}
              </v-list-item-title>
              <v-list-item-subtitle>
                {{ appStore.user?.email }}
              </v-list-item-subtitle>
            </v-list-item>
          </v-list>

          <v-divider></v-divider>

          <v-list>
            <v-list-item
              prepend-icon="mdi-account"
              title="Profile"
              @click="userMenu = false"
            ></v-list-item>
            <v-list-item
              prepend-icon="mdi-cog"
              title="Settings"
              @click="userMenu = false"
            ></v-list-item>
            <v-divider></v-divider>
            <v-list-item
              prepend-icon="mdi-logout"
              title="Sign Out"
              @click="handleLogout"
            ></v-list-item>
          </v-list>
        </v-card>
      </v-menu>
    </v-app-bar>

    <!-- Main Content -->
    <v-main class="bg-background">
      <v-container fluid class="pa-6">
        <router-view />
      </v-container>
    </v-main>

    <!-- Loading Overlay -->
    <v-overlay
      v-model="appStore.isLoading"
      class="align-center justify-center"
    >
      <v-progress-circular
        size="64"
        color="primary"
        indeterminate
      ></v-progress-circular>
    </v-overlay>
  </v-app>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAppStore } from '@/stores/app'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const appStore = useAppStore()
const authStore = useAuthStore()

// Local state
const rail = ref(true)
const searchQuery = ref('')
const userMenu = ref(false)
const notificationsCount = ref(3)

// All navigation items
const allNavigationItems = [
{
    title: 'Solicitar Préstamo',
    subtitle: 'Solicitar un préstamo',
    icon: 'mdi-file-document-plus',
    to: '/solicitar',
    roles: ['user']
  },
  {
    title: 'Solicitudes',
    subtitle: 'Ver solicitudes de préstamos',
    icon: 'mdi-file-document',
    to: '/solicitudes',
    roles: ['admin']
  },
  {
    title: 'Inventario',
    subtitle: 'Gestionar el inventario',
    icon: 'mdi-package-variant',
    to: '/inventario',
    roles: ['admin']
  },
  {
    title: 'Préstamos',
    subtitle: 'Gestionar los préstamos',
    icon: 'mdi-history',
    to: '/prestamos',
    roles: ['admin']
  },
  {
    title: 'Usuarios',
    subtitle: 'Gestionar los usuarios',
    icon: 'mdi-account-group',
    to: '/usuarios',
    roles: ['admin']
  },
]

// Filtered navigation items based on user role
const userNavigationItems = computed(() => {
  return allNavigationItems.filter(item => 
    item.roles.includes(appStore.user?.role ?? 'user')
  )
})

// Current page title
const currentPageTitle = computed(() => {
  const currentRoute = router.currentRoute.value
  const item = userNavigationItems.value.find(item => item.to === currentRoute.path)
  return item?.title || 'Prestamos CEII'
})

// Handle logout
const handleLogout = () => {
  authStore.logout()
  userMenu.value = false
  // Router guards will handle redirect to login
}

// No need for authentication checks here since router guards handle it
</script>

<style scoped>
.max-width-300 {
  max-width: 300px;
}

.v-list-item--active {
  background-color: rgb(var(--v-theme-primary-lighten-5));
  color: rgb(var(--v-theme-primary));
}

.v-list-item--active .v-list-item__prepend .v-icon {
  color: rgb(var(--v-theme-primary));
}

/* Better text wrapping for long titles */
.v-list-item__title {
  white-space: normal;
  line-height: 1.2;
}

.v-list-item__subtitle {
  white-space: normal;
  line-height: 1.1;
}
</style>
  