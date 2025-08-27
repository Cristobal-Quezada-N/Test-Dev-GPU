<template>
  <v-container>
    <v-card class="pa-4" elevation="2">
      <v-card-title>
        Inventario de Juegos
        <v-spacer></v-spacer>
        <v-text-field
          v-model="search"
          label="Buscar"
          prepend-icon="mdi-magnify"
          hide-details
          clearable
        />
      </v-card-title>

      <!-- Tabla de juegos -->
      <v-data-table-virtual
        :headers="headers"
        :items="items"
        :search="search"
        fixed-header
        class="elevation-1"
      >
        <!-- stock -->
        <template v-slot:item.stock="{ item }">
          <span>{{ item.stock }}</span>
        </template>

        <!-- estado disponible -->
        <template v-slot:item.available="{ item }">
          <v-chip :color="item.available ? 'green' : 'red'" dark>
            {{ item.available ? "Disponible" : "No disponible" }}
          </v-chip>
        </template>

        <!-- acciones -->
        <template v-slot:item.actions="{ item }">
          <v-btn icon color="blue" @click="openDialog(item)">
            <v-icon>mdi-pencil</v-icon>
          </v-btn>
          <v-divider vertical class="mx-2"></v-divider>
          <v-btn icon color="red" @click="confirmDelete(item)">
            <v-icon>mdi-delete</v-icon>
          </v-btn>
        </template>
      </v-data-table-virtual>

      <!-- boton agregar -->
      <div class="d-flex justify-end mt-4">
        <v-chip
          color="primary"
          text-color="white"
          pill
          @click="openDialog()"
        >
          <v-icon start>mdi-plus</v-icon>
          Agregar Juego
        </v-chip>
      </div>
    </v-card>

    <!-- dialogo de edición -->
    <ItemDialog v-model="dialog" :item="selectedItem" @save="saveItem" />

    <!-- dialogo confirmación borrado -->
    <v-dialog v-model="confirmDialog" max-width="400px">
      <v-card>
        <v-card-title class="headline">Confirmar eliminación</v-card-title>
        <v-card-text>
          ¿Estás seguro que deseas eliminar el juego
          <strong>{{ itemToDelete?.name }}</strong>?
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey" text @click="confirmDialog = false">Cancelar</v-btn>
          <v-btn color="red" text @click="deleteConfirmed">Eliminar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import axios from "axios";
import ItemDialog from "../components/ItemDialog.vue";

interface Item {
  id: number;
  name: string;
  stock: number;
  available: boolean;
}

const search = ref("");
const dialog = ref(false);
const selectedItem = ref<Item | undefined>(undefined);

const confirmDialog = ref(false);
const itemToDelete = ref<Item | null>(null);

const headers = [
  { title: "ID", key: "id", align: "start" },
  { title: "Nombre", key: "name", align: "start" },
  { title: "Stock", key: "stock", align: "end" },
  { title: "Estado", key: "available", align: "center" },
  { title: "Acciones", key: "actions", align: "center", sortable: false },
];

const items = ref<Item[]>([]);

const fetchItems = async () => {
  try {
    const response = await axios.get<Item[]>(
      "http://localhost:8090/api/items/getItems"
    );
    items.value = response.data;
  } catch (error) {
    console.error("Error al obtener el inventario:", error);
  }
};

const openDialog = (item?: Item) => {
  selectedItem.value = item ? { ...item } : undefined;
  dialog.value = true;
};

const confirmDelete = (item: Item) => {
  itemToDelete.value = item;
  confirmDialog.value = true;
};

const deleteConfirmed = async () => {
  if (!itemToDelete.value) return;
  try {
    await axios.delete(
      `http://localhost:8090/api/items/${itemToDelete.value.id}`
    );
    items.value = items.value.filter((item) => item.id !== itemToDelete.value?.id);
  } catch (error) {
    console.error("Error al eliminar el juego:", error);
  } finally {
    confirmDialog.value = false;
    itemToDelete.value = null;
  }
};

const saveItem = async (item: Item) => {
  try {
    if (item.id) {
      const response = await axios.put(
        `http://localhost:8090/api/items/updateItem/${item.id}`,
        item
      );
      const index = items.value.findIndex((i) => i.id === item.id);
      if (index !== -1) items.value[index] = response.data;
    } else {
      const response = await axios.post(
        "http://localhost:8090/api/items/createItem",
        item
      );
      items.value.push(response.data);
    }
  } catch (error) {
    console.error("Error al guardar el juego:", error);
  }
};

onMounted(fetchItems);
</script>
