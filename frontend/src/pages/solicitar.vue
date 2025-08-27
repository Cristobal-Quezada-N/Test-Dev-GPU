<template>
  <v-container>
    <v-card class="pa-4" elevation="2">
      <div class="text-caption mb-2" style="color: gray;">
        Fecha local: {{ fechaLocal }}
      </div>
      <v-card-title>
        Solicitudes de Préstamos
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
          <v-btn color="amber-lighten-2" @click="confirmarLoan(item)"> Solicitar
          </v-btn>
          <v-divider vertical class="mx-2"></v-divider>
        </template>
      </v-data-table-virtual>
    </v-card>

    <v-dialog v-model="confirmLoan" max-width="400px">
      <v-card>
        <v-card-title class="headline">Solicitar préstamo</v-card-title>
        <v-card-text>
          <div>
            ¿Cuánto tiempo estimas usar el juego <strong>{{ selectedItem?.name }}</strong>?
          </div>
          <v-text-field
            v-model="tiempoEstimado"
            label="Tiempo estimado (horas)"
            type="number"
            min="1"
            required
            class="mt-4"
          />
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="grey" text @click="confirmLoan = false">Cancelar</v-btn>
          <v-btn color="primary" text @click="solicitarLoan">Solicitar</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import axios from "axios";

interface Item {
  id: number;
  name: string;
  stock: number;
  available: boolean;
}

const fechaLocal = ref(new Date().toLocaleString());
const search = ref("");
const selectedItem = ref<Item | undefined>(undefined);
const confirmLoan = ref(false);

const headers = [
  { title: "ID", key: "id", align: "start" },
  { title: "Nombre", key: "name", align: "start" },
  { title: "Stock", key: "stock", align: "end" },
  { title: "Estado", key: "available", align: "center" },
  { title: "Acciones", key: "actions", align: "center", sortable: false },
];

const items = ref<Item[]>([]);

const tiempoEstimado = ref<number | null>(null);

const solicitarLoan = async () => {
  if (!selectedItem.value || !tiempoEstimado.value) {
    alert("Por favor ingresa el tiempo estimado.");
    return;
  }
  try {
    // Aquí iría la lógica para solicitar el préstamo con el tiempo estimado
    // await axios.post("http://localhost:8090/api/loans/request", { itemId: selectedItem.value.id, tiempo: tiempoEstimado.value });
    alert(`Solicitud enviada para "${selectedItem.value.name}" por ${tiempoEstimado.value} horas.`);
    confirmLoan.value = false;
    tiempoEstimado.value = null;
  } catch (error) {
    console.error("Error al solicitar el préstamo:", error);
    alert("Error al solicitar el préstamo. Inténtalo de nuevo.");
  }
};

const confirmarLoan = (item: Item) => {
  selectedItem.value = item;
  confirmLoan.value = true;
};

const fetchItems = async () => {
  try {
    const response = await axios.get<Item[]>(
      "http://localhost:8090/api/items/getItems"
    );
    items.value = response.data;

    console.log("Items fetched:", items.value);
  } catch (error) {
    console.error("Error al obtener el inventario:", error);
  }
};



onMounted(() => {
  fetchItems();
  fechaLocal.value = new Date().toLocaleString();
});
</script>
