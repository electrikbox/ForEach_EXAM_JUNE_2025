<script setup lang="ts">
import { ref, computed, watch, nextTick } from 'vue'
import { usePanierStore } from '../stores/panierStore'

interface Props {
  visible: boolean
  cocktail: any
}

interface Emits {
  (e: 'close'): void
}

const props = defineProps<Props>()
const emit = defineEmits<Emits>()

const panierStore = usePanierStore()

const selectedTaille = ref<any>(null)
const quantite = ref(1)

const sortedTaillesPrix = computed(() => {
  if (!props.cocktail?.taillesPrix) return [];
  return [...props.cocktail.taillesPrix].sort((a, b) => a.prix - b.prix);
});

const prixTotal = computed(() => {
  if (!selectedTaille.value) return 0
  return selectedTaille.value.prix * quantite.value
})

const fermerModal = () => {
  emit('close')
  selectedTaille.value = null
  quantite.value = 1
}

const modifierQuantite = (delta: number) => {
  const nouvelleQuantite = quantite.value + delta
  if (nouvelleQuantite >= 1) {
    quantite.value = nouvelleQuantite
  }
}

const ajouterAuPanier = () => {
  if (!selectedTaille.value || !props.cocktail) return

  panierStore.ajouterAuPanier(
    props.cocktail,
    selectedTaille.value,
    quantite.value
  )
  fermerModal()
}

// Watcher pour initialiser selectedTaille lorsque le cocktail devient visible
watch(() => props.visible, (newVal) => {
  if (newVal && props.cocktail?.taillesPrix?.length) {
    nextTick(() => {
      // Initialise avec la taille la plus grande (dernière après tri par prix croissant)
      selectedTaille.value = sortedTaillesPrix.value[sortedTaillesPrix.value.length - 1];
      quantite.value = 1;
    });
    document.body.style.overflow = 'hidden'; // Empêche le défilement de la page derrière
  } else {
    document.body.style.overflow = ''; // Rétablit le défilement
  }
}, { immediate: true });
</script>

<template>
  <div v-if="visible" 
       class="fixed inset-0 flex justify-center items-center z-50 p-0 sm:p-1 backdrop-blur-sm bg-white/30" 
       @click="fermerModal">
    <div 
      class="bg-white rounded-none w-full h-full overflow-y-auto shadow-none relative
             sm:rounded-lg sm:max-w-md sm:h-auto sm:shadow-2xl sm:p-1" @click.stop
    >
      <button 
        class="absolute top-4 right-4 text-gray-600 hover:text-gray-900 text-3xl leading-none p-1" 
        @click="fermerModal"
      >
        &times;
      </button>
  
      <div class="pt-8 pb-5 px-6 border-b border-gray-200">
        <h1 class="text-3xl font-bold text-gray-900 mb-2">{{ cocktail?.nomCocktail }}</h1>
        <p class="text-base text-gray-700 leading-relaxed mb-3">{{ cocktail?.descriptionCocktail }}</p>
        
        <div 
          v-if="cocktail?.categorie" 
          class="inline-block bg-gray-200 text-gray-700 text-sm font-medium px-3 py-1 rounded-full"
        >
          {{ cocktail.categorie.nomCategorie }}
        </div>
      </div>
  
      <div class="p-6">
        <div class="mb-5"> 
          <h2 class="text-xl font-semibold text-gray-800 pb-2 mb-2 border-b border-gray-200">Ingredients</h2> 
          <div class="flex flex-col space-y-1"> 
            <div 
              v-for="ingredient in cocktail?.ingredients" 
              :key="ingredient.idIngredient"
              class="flex justify-between text-gray-700 py-1 border-b border-gray-100 last:border-b-0" 
            >
              <span class="font-medium text-gray-800 text-sm">{{ ingredient.nomIngredient }}</span> 
              <span class="text-gray-600 text-sm">{{ ingredient.quantite }} {{ ingredient.unite }}</span> 
            </div>
          </div>
        </div>
  
        <div class="mb-5"> 
          <div class="flex items-center gap-3 pb-2 mb-2 border-b border-gray-200"> 
            <h2 class="text-xl font-semibold text-gray-800 whitespace-nowrap">Size</h2>
            <div class="flex flex-wrap gap-2"> 
              <button
                v-for="taille in sortedTaillesPrix"
                :key="taille.idTaille"
                @click="selectedTaille = taille"
                :class="{ 
                  'bg-yellow-400 text-gray-900 border-none font-bold shadow-md': selectedTaille?.idTaille === taille.idTaille,
                  'bg-white text-gray-800 border-gray-300 hover:bg-gray-50': selectedTaille?.idTaille !== taille.idTaille
                }"
                class="py-1.5 px-4 rounded-full border w-[3.5em] text-sm transition-all duration-200 ease-in-out font-medium" 
              >
                {{ taille.nomTaille }}
              </button>
            </div>
          </div>
        </div>
  
        <div class="mb-5"> 
          <div class="flex items-center gap-3 pb-2 mb-2 border-b border-gray-200"> 
            <h2 class="text-xl font-semibold text-gray-800 whitespace-nowrap">Quantity</h2>
            <div class="flex items-center space-x-1 border border-gray-300 rounded-lg w-fit p-0.5"> 
              <button 
                @click="modifierQuantite(-1)" 
                :disabled="quantite <= 1"
                class="w-7 h-7 flex items-center justify-center text-gray-700 text-xl rounded-md hover:bg-gray-100 disabled:opacity-40 disabled:cursor-not-allowed" 
              >
                -
              </button>
              <span class="text-lg font-semibold text-gray-800 w-6 text-center">{{ quantite }}</span> 
              <button 
                @click="modifierQuantite(1)" 
                class="w-7 h-7 flex items-center justify-center text-gray-700 text-xl rounded-md hover:bg-gray-100" 
              >
                +
              </button>
            </div>
          </div>
        </div>
  
        <div class="mb-6"> 
          <h2 class="text-xl font-semibold text-gray-800">Price</h2>
          <span class="block text-3xl font-bold text-gray-900 mt-1"> 
            {{ prixTotal.toFixed(2) }}€
          </span>
        </div>
  
        <button 
          @click="ajouterAuPanier"
          :disabled="!selectedTaille || quantite <= 0"
          class="w-full py-4 bg-yellow-400 text-gray-900 font-bold text-lg rounded-xl shadow-md hover:bg-yellow-500 transition-colors duration-200 disabled:bg-gray-300 disabled:text-gray-600 disabled:cursor-not-allowed"
        >
          Add to Cart
        </button>
      </div>
    </div>
  </div>
</template>

