<script setup>
import { ref, onMounted } from 'vue'
import { useCocktailStore } from '@/stores/cocktailStore'
import { storeToRefs } from 'pinia'
import { getCocktailDetails } from '@/api'
import CocktailDetailModal from '@/components/CocktailDetailModal.vue'

const cocktailStore = useCocktailStore()
const { categories, selectedCategory, filteredCocktails, isLoading, error } = storeToRefs(cocktailStore)

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
    cocktailDetails.value = {
      ...res.data.cocktail,
      ingredients: res.data.ingredients,
      taillesPrix: res.data.taillesPrix
    }
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
  await cocktailStore.fetchAllData();
})
</script>

<template>
  <div class="min-h-screen py-8 px-4 sm:px-6 lg:px-8">
    <div :class="['w-full max-w-5xl mx-auto', showModal ? 'blur-bg pointer-events-none select-none' : '']">
      <h1 class="text-4xl font-bold text-gray-900 text-center mb-8">Cocktails</h1>

      <div class="flex flex-nowrap overflow-x-auto gap-3 mb-10 pb-2 custom-scrollbar
                  md:flex-wrap md:justify-center md:overflow-x-visible">
        <button
          @click="cocktailStore.setSelectedCategory(null)"
          :class="['flex-shrink-0 px-5 py-2 rounded-full font-medium transition-colors duration-200', selectedCategory === null ? 'bg-yellow-400 text-gray-900 shadow-md' : 'bg-gray-200 text-gray-700 hover:bg-gray-300']"
        >
          Toutes
        </button>
        <button
          v-for="category in categories"
          :key="category.idCategorie"
          @click="cocktailStore.setSelectedCategory(category.idCategorie)"
          :class="['flex-shrink-0 px-5 py-2 rounded-full font-medium transition-colors duration-200', selectedCategory === category.idCategorie ? 'bg-yellow-400 text-gray-900 shadow-md' : 'bg-gray-200 text-gray-700 hover:bg-gray-300']"
        >
          {{ category.nomCategorie }}
        </button>
      </div>
      <div v-if="isLoading" class="text-center py-20 text-gray-600 text-lg">Chargement des cocktails...</div>
      <div v-else-if="error" class="text-center py-20 text-red-500 text-lg">{{ error }}</div>
      <div v-else-if="filteredCocktails.length === 0" class="text-center py-20 text-gray-600 text-lg">
        Aucun cocktail trouvé pour cette catégorie.
      </div>
      
      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div
          v-for="cocktail in filteredCocktails"
          :key="cocktail.idCocktail"
          class="cocktail-card bg-white rounded-xl shadow-md overflow-hidden cursor-pointer transform transition-all duration-200 hover:scale-[1.01] hover:shadow-lg"
          @click="openModal(cocktail)"
        >
          <div class="flex flex-row md:flex-col items-center md:items-start p-4">
            <img
              :src="cocktail.imageUrl || 'https://via.placeholder.com/150?text=Cocktail'"
              :alt="cocktail.nomCocktail"
              class="w-24 h-24 sm:w-32 sm:h-32 md:w-full md:h-48 object-cover rounded-lg md:rounded-b-none md:rounded-t-lg mr-4 md:mr-0 md:mb-4 flex-shrink-0"
            />
            
            <div class="flex-grow md:p-0">
              <h2 class="text-xl font-bold text-gray-900 mb-1">{{ cocktail.nomCocktail }}</h2>
              <p class="text-gray-600 text-sm mb-2 line-clamp-2">{{ cocktail.descriptionCocktail }}</p>
              <p class="text-gray-500 text-xs italic mb-3">{{ cocktail.categorie?.nomCategorie }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <CocktailDetailModal
      :visible="showModal && !loadingDetails && !errorDetails"
      :cocktail="cocktailDetails" @close="closeModal"
    />

    <transition name="modal-slide-down">
      <div v-if="showModal && (loadingDetails || errorDetails)" class="fixed inset-0 flex items-center justify-center z-50 p-4">
        <div class="absolute inset-0 bg-black bg-opacity-40" @click="closeModal"></div>
        <div class="bg-white rounded-lg shadow-lg p-6 min-w-[300px] max-w-lg w-full relative z-10 animate-slide-down">
          <button class="absolute top-2 right-2 text-gray-500 hover:text-black text-2xl" @click="closeModal">&times;</button>
          <template v-if="loadingDetails">
            <div class="text-center py-8 text-gray-700">Chargement...</div>
          </template>
          <template v-else-if="errorDetails">
            <div class="text-red-500 text-center py-4">{{ errorDetails }}</div>
          </template>
        </div>
      </div>
    </transition>
  </div>
</template>

<style scoped>
/* Conserver les styles scoped existants */
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

/* Styles pour masquer la barre de défilement */
.custom-scrollbar::-webkit-scrollbar {
  height: 6px; /* Ajustez la hauteur selon vos préférences */
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent; /* Fond transparent pour la piste */
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: rgba(156, 163, 175, 0.5); /* Couleur du pouce (gris semi-transparent) */
  border-radius: 3px; /* Coins arrondis pour le pouce */
}

/* Pour Firefox */
.custom-scrollbar {
  scrollbar-width: thin;
  scrollbar-color: rgba(156, 163, 175, 0.5) transparent;
}
</style>