<script setup>
import { ref } from 'vue'

const cocktails = ref([
  { name: 'Cocktail 1', description: 'Description 1' },
  { name: 'Cocktail 2', description: 'Description 2' },
  { name: 'Cocktail 3', description: 'Description 3' },
  { name: 'Cocktail 4', description: 'Description 4' },
  { name: 'Cocktail 5', description: 'Description 5' },
  { name: 'Cocktail 6', description: 'Description 6' },
  { name: 'Cocktail 7', description: 'Description 7' },
  { name: 'Cocktail 8', description: 'Description 8' },
  { name: 'Cocktail 9', description: 'Description 9' },
  { name: 'Cocktail 10', description: 'Description 10' },
])
</script>

<template>
  <div class="w-full max-w-4xl mx-auto p-2 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
    <div v-for="(cocktail, idx) in cocktails" :key="idx" class="card bg-white shadow-sm rounded-sm p-4 flex flex-col items-center w-full">
      <h1 class="text-lg font-bold mb-2">{{ cocktail.name }}</h1>
      <p>{{ cocktail.description }}</p>
    </div>
  </div>
</template>
