<template>
  <div class="loading-screen">
    <div class="loading-content">
      <v-progress-circular
        size="64"
        color="primary"
        indeterminate
        class="mb-4"
      ></v-progress-circular>
      <h2 class="text-h5 font-weight-bold text-primary mb-2">
        Prestamos CEII
      </h2>
      <p class="text-body-1 text-medium-emphasis">
        Inicializando sistema...
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
// Loading screen component for authentication initialization
</script>

<style scoped>
.loading-screen {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, #00A499 0%, #EA7600 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.loading-content {
  text-align: center;
  background: rgba(255, 255, 255, 0.95);
  padding: 48px;
  border-radius: 16px;
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
}
</style>
