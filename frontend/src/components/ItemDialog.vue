<template>
  <v-dialog v-model="dialogProxy" max-width="500px">
    <v-card>
      <v-card-title>{{ localItem.id ? "Editar Juego" : "Agregar Juego" }}</v-card-title>
      <v-card-text>
        <v-text-field v-model="localItem.name" label="Nombre" />
        <v-text-field v-model="localItem.stock" label="Stock" type="number" />
        <v-switch
          v-model="localItem.available"
          base-color="red"
          color="green"
          label="Disponible"
        />
      </v-card-text>
      <v-card-actions>
        <v-spacer />
        <v-btn text @click="cancel">
          Cancelar
        </v-btn>
        <v-btn color="primary" @click="save">
          Guardar
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
  import { computed, defineEmits, defineProps, ref, watch } from 'vue'

  interface Item {
    id?: number
    name: string
    stock: number
    available: boolean
  }

  const props = defineProps<{
    modelValue: boolean
    item?: Item
  }>()

  const emit = defineEmits(['update:modelValue', 'save'])

  const dialogProxy = computed({
    get: () => props.modelValue,
    set: (value: boolean) => emit('update:modelValue', value),
  })

  const localItem = ref<Item>({ name: '', stock: 0, available: true })

  watch(
    () => props.item,
    newItem => {
      localItem.value = newItem ? { ...newItem } : { name: '', stock: 0, available: true }
    },
    { immediate: true },
  )

  const cancel = () => {
    emit('update:modelValue', false)
  }

  const save = () => {
    emit('save', localItem.value)
    emit('update:modelValue', false)
    localItem.value = { name: '', stock: 0, available: true }
  }
</script>
