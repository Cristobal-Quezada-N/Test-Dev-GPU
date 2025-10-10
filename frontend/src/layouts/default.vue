<template>
  <v-app>
    <!-- Navigation Drawer -->
    <v-navigation-drawer
      v-model="appStore.drawer"
      class="elevation-2"
      permanent
      :rail="rail"
      @mouseenter="rail = false"
      @mouseleave="rail = true"
    >
      <!-- Logo Section -->
      <v-list>
        <v-list-item>
          <template #prepend>
            <v-btn variant="text" class="d-flex align-center" @click="router.push('/')">
              <v-icon color="primary" size="32">
                mdi-handshake
              </v-icon>
              <span class="text-h6 font-weight-bold text-primary ml-2">
                Prestamos CEII
              </span>
            </v-btn>
          </template>
        </v-list-item>
      </v-list>

      <v-divider class="mb-2" />

      <!-- Navigation Menu based on user role -->
      <template v-if="appStore.isAdmin">
        <!-- Admin Navigation -->
        <v-list density="compact" nav>
          <v-list-item
            v-for="item in userNavigationItems"
            :key="item.title"
            :class="{ 'v-list-item--active': $route.path === item.to }"
            :prepend-icon="item.icon"
            rounded="lg"
            :subtitle="item.subtitle"
            :title="item.title"
            :to="item.to"
          />
        </v-list>
      </template>

      <template v-else>
        <!-- User Navigation -->
        <v-list density="compact" nav>
          <v-list-item
            v-for="item in userNavigationItems"
            :key="item.title"
            :class="{ 'v-list-item--active': $route.path === item.to }"
            :prepend-icon="item.icon"
            rounded="lg"
            :subtitle="item.subtitle"
            :title="item.title"
            :to="item.to"
          />
        </v-list>
      </template>

      <!-- Logout Button -->
      <v-spacer />
      <v-list class="mt-auto" density="compact" nav>
        <v-list-item
          class="mb-2"
          prepend-icon="mdi-logout"
          rounded="lg"
          subtitle="Salir del sistema"
          title="Cerrar Sesión"
          @click="handleLogout"
        />
      </v-list>
    </v-navigation-drawer>

    <!-- Top App Bar -->
    <v-app-bar
      class="border-b"
      color="surface"
      elevation="1"
    >
      <v-app-bar-nav-icon
        class="d-md-none"
        @click="appStore.toggleDrawer"
      />

      <v-app-bar-title class="text-h6 font-weight-bold text-primary">
        {{ currentPageTitle }}
      </v-app-bar-title>

      <v-spacer />

      <!-- User Menu -->
      <v-menu
        v-model="userMenu"
        :close-on-content-click="false"
        location="bottom end"
      >
        <template #activator="{ props }">
          <v-btn
            v-bind="props"
            class="text-none"
            variant="text"
          >
            <v-avatar
              class="mr-2"
              color="primary"
              size="32"
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

        <v-card class="elevation-8" min-width="200">
          <v-list>
            <v-list-item>
              <template #prepend>
                <v-avatar
                  color="primary"
                  size="40"
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

          <v-divider />

          <v-list>
            <v-divider />
            <v-list-item
              prepend-icon="mdi-logout"
              title="Sign Out"
              @click="handleLogout"
            />
          </v-list>
        </v-card>
      </v-menu>
    </v-app-bar>

    <!-- Main Content -->
    <v-main class="bg-background">
      <v-container class="pa-6" fluid>
        <router-view />
      </v-container>
    </v-main>

    <!-- Loading Overlay -->
    <v-overlay
      v-model="appStore.isLoading"
      class="align-center justify-center"
    >
      <v-progress-circular
        color="primary"
        indeterminate
        size="64"
      />
    </v-overlay>
  </v-app>
</template>

<script lang="ts" setup>
  import { computed, ref } from 'vue'
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
      roles: ['user'],
    },
    {
      title: 'Solicitudes',
      subtitle: 'Ver solicitudes de préstamos',
      icon: 'mdi-file-document',
      to: '/solicitudes',
      roles: ['admin'],
    },
    {
      title: 'Inventario',
      subtitle: 'Gestionar el inventario',
      icon: 'mdi-package-variant',
      to: '/inventario',
      roles: ['admin'],
    },
    {
      title: 'Préstamos',
      subtitle: 'Gestionar los préstamos',
      icon: 'mdi-history',
      to: '/prestamos',
      roles: ['admin'],
    },
    {
      title: 'Usuarios',
      subtitle: 'Gestionar los usuarios',
      icon: 'mdi-account-group',
      to: '/usuarios',
      roles: ['admin'],
    },
  ]

  // Filtered navigation items based on user role
  const userNavigationItems = computed(() => {
    return allNavigationItems.filter(item =>
      item.roles.includes(appStore.user?.role ?? 'user'),
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
  router.push("/login") // 🚀 Ir directo al login
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
