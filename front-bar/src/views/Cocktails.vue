<script setup>
import { ref, onMounted } from 'vue'
import { useCocktailStore } from '@/stores/cocktailStore'
import { storeToRefs } from 'pinia'
import { getCocktailDetails } from '@/api'

const cocktailStore = useCocktailStore()
const { cocktails } = storeToRefs(cocktailStore)

const showModal = ref(false)
const selectedCocktail = ref(null)
const cocktailDetails = ref(null)
const loadingDetails = ref(false)
const errorDetails = ref(null)

async function openModal(cocktail) {
  selectedCocktail.value = cocktail
  showModal.value = true
  loadingDetails.value = true
  errorDetails.value = null
  try {
    const res = await getCocktailDetails(cocktail.idCocktail)
    cocktailDetails.value = res.data
  } catch (e) {
    errorDetails.value = e.message || 'Erreur lors du chargement des détails'
  } finally {
    loadingDetails.value = false
  }
}
function closeModal() {
  showModal.value = false
  selectedCocktail.value = null
  cocktailDetails.value = null
  errorDetails.value = null
}

onMounted(async () => {
  await cocktailStore.fetchCocktails()
  console.log(cocktails.value)
})
</script>

<template>
  <div>
    <!-- Grille floutée -->
    <div :class="['w-full max-w-4xl mx-auto p-2 grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4', showModal ? 'blur-bg pointer-events-none select-none' : '']">
      <div v-for="(cocktail, idx) in cocktails" :key="idx" class="card bg-white shadow-sm rounded-sm p-4 flex flex-col items-center w-full">
        <h1 class="text-lg font-bold mb-2">{{ cocktail.nomCocktail }}</h1>
        <p>{{ cocktail.descriptionCocktail }}</p>
        <p>{{ cocktail.categorie.nomCategorie }}</p>
        <button class="mt-2 px-3 py-1 bg-primary text-white rounded hover:bg-secondary" @click="openModal(cocktail)">Détails</button>
      </div>
    </div>

    <!-- Modal et overlay en dehors de la grille -->
    <transition name="modal-slide-down">
      <div v-if="showModal" class="fixed inset-0 flex items-center justify-center z-50">
        <div class="absolute inset-0" @click="closeModal"></div>
        <div class="bg-white rounded shadow-lg p-6 min-w-[300px] max-w-[90vw] relative z-10 animate-slide-down">
          <button class="absolute top-2 right-2 text-gray-500 hover:text-black" @click="closeModal">&times;</button>
          <template v-if="loadingDetails">
            <div class="text-center py-8">Chargement...</div>
          </template>
          <template v-else-if="errorDetails">
            <div class="text-red-500">{{ errorDetails }}</div>
          </template>
          <template v-else-if="cocktailDetails">
            <h2 class="text-xl font-bold mb-2">{{ cocktailDetails.cocktail.nomCocktail }}</h2>
            <p class="mb-2">{{ cocktailDetails.cocktail.descriptionCocktail }}</p>
            <p class="mb-2"><strong>Catégorie :</strong> {{ cocktailDetails.cocktail.categorie?.nomCategorie }}</p>
            <div class="mb-2">
              <strong>Ingrédients :</strong>
              <ul class="list-disc ml-6">
                <li v-for="ing in cocktailDetails.ingredients" :key="ing.idIngredient">
                  {{ ing.nomIngredient }} : {{ ing.quantite }} {{ ing.unite }}
                </li>
              </ul>
            </div>
            <div class="mb-2">
              <strong>Tailles & Prix :</strong>
              <ul class="list-disc ml-6">
                <li v-for="tp in cocktailDetails.taillesPrix" :key="tp.idTaille">
                  {{ tp.nomTaille }} : {{ tp.prix }} €
                </li>
              </ul>
            </div>
          </template>
        </div>
      </div>
    </transition>
  </div>
</template>

<style scoped>
.blur-bg {
  filter: blur(6px);
  transition: filter 0.35s cubic-bezier(0.4, 0, 0.2, 1);
}
.pointer-events-none {
  pointer-events: none;
}
.select-none {
  user-select: none;
}

.modal-slide-down-enter-active {
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
}
.modal-slide-down-leave-active {
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}
.modal-slide-down-enter-from {
  opacity: 0;
  transform: translateY(-60px) scale(0.98);
}
.modal-slide-down-enter-to {
  opacity: 1;
  transform: translateY(0) scale(1);
}
.modal-slide-down-leave-from {
  opacity: 1;
  transform: translateY(0) scale(1);
}
.modal-slide-down-leave-to {
  opacity: 0;
  transform: translateY(-60px) scale(0.98);
}
</style>
