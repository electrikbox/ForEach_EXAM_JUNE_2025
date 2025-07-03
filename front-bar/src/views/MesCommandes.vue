<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/userStore'
import { getCommandesUtilisateur } from '../api'

interface LigneCommande {
  idLigneCommande: number
  nomCocktail: string
  nomTaille: string
  quantite: number
  prix: number
  statutCocktailPreparation: string
}

interface Commande {
  idCommande: number
  dateCommande: string
  statutCommande: string
  lignes: LigneCommande[]
  total: number
}

const userStore = useUserStore()
const commandes = ref<Commande[]>([])
const chargementEnCours = ref(false)

const chargerCommandes = async () => {
  if (!userStore.user) return
  
  chargementEnCours.value = true
  
  try {
    const response = await getCommandesUtilisateur()
    commandes.value = response.data
  } catch (error) {
    console.error('Erreur chargement commandes:', error)
    alert('Erreur lors du chargement des commandes')
  } finally {
    chargementEnCours.value = false
  }
}

const formaterDate = (dateString: string) => {
  const date = new Date(dateString)
  return date.toLocaleDateString('fr-FR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

const getStatutLabel = (statut: string) => {
  const statuts: Record<string, string> = {
    'Commandée': 'Commandée',
    'en cours de préparation': 'En préparation',
    'Terminée': 'Terminée'
  }
  return statuts[statut] || statut
}

onMounted(() => {
  if (userStore.user) {
    chargerCommandes()
  }
})
</script>

<template>
  <div class="py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-6xl mx-auto">
      <div class="flex justify-between items-center mb-8 pb-4 border-b border-gray-200">
        <h1 class="text-4xl font-bold text-gray-900">Mes Commandes</h1>
      </div>

      <!-- Chargement -->
      <div v-if="chargementEnCours" class="flex flex-col items-center justify-center py-20">
        <div class="w-12 h-12 border-4 border-yellow-400 border-t-transparent rounded-full animate-spin mb-4"></div>
        <p class="text-gray-600">Chargement de vos commandes...</p>
      </div>

      <!-- Non connecté -->
      <div v-else-if="!userStore.user" class="bg-white rounded-xl shadow-sm p-8 text-center">
        <div class="max-w-md mx-auto">
          <svg class="w-16 h-16 text-gray-400 mx-auto mb-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
            <path fill="currentColor" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
          </svg>
          <h2 class="text-2xl font-bold text-gray-900 mb-2">Connexion requise</h2>
          <p class="text-gray-600 mb-6">Vous devez être connecté pour voir vos commandes</p>
          <RouterLink 
            to="/login" 
            class="inline-block bg-yellow-400 text-gray-900 font-bold px-6 py-3 rounded-lg hover:bg-yellow-500 transition-colors duration-200"
          >
            Se connecter
          </RouterLink>
        </div>
      </div>

      <!-- Liste des commandes -->
      <div v-else-if="commandes.length > 0" class="space-y-6">
        <div 
          v-for="commande in commandes" 
          :key="commande.idCommande" 
          class="bg-white rounded-xl shadow-sm overflow-hidden"
        >
          <div class="p-6 border-b border-gray-100">
            <div class="flex justify-between items-start mb-4">
              <div>
                <h3 class="text-lg font-bold text-gray-900">Commande #{{ commande.idCommande }}</h3>
                <p class="text-sm text-gray-600">{{ formaterDate(commande.dateCommande) }}</p>
              </div>
              <span 
                :class="[
                  'px-3 py-1 rounded-full text-sm font-medium',
                  {
                    'bg-yellow-100 text-yellow-800': commande.statutCommande === 'Commandée',
                    'bg-blue-100 text-blue-800': commande.statutCommande === 'en cours de préparation',
                    'bg-green-100 text-green-800': commande.statutCommande === 'Terminée'
                  }
                ]"
              >
                {{ getStatutLabel(commande.statutCommande) }}
              </span>
            </div>

            <div class="space-y-3">
              <div 
                v-for="ligne in commande.lignes" 
                :key="ligne.idLigneCommande" 
                class="flex justify-between items-center py-2"
              >
                <div class="flex-1">
                  <div class="flex items-baseline">
                    <span class="text-gray-900 font-medium">{{ ligne.nomCocktail }}</span>
                    <span class="ml-2 text-sm text-gray-600">{{ ligne.nomTaille }}</span>
                  </div>
                </div>
                <div class="text-right">
                  <div class="text-gray-900">{{ ligne.quantite }}x</div>
                  <div class="text-sm text-gray-600">{{ (ligne.prix * ligne.quantite).toFixed(2) }}€</div>
                </div>
              </div>
            </div>
          </div>

          <div class="bg-gray-50 px-6 py-4">
            <div class="flex justify-between items-center">
              <span class="font-medium text-gray-900">Total</span>
              <span class="text-xl font-bold text-yellow-500">{{ commande.total.toFixed(2) }}€</span>
            </div>
          </div>
        </div>
      </div>

      <!-- Aucune commande -->
      <div v-else class="bg-white rounded-xl shadow-sm p-8 text-center">
        <div class="max-w-md mx-auto">
          <svg class="w-16 h-16 text-gray-400 mx-auto mb-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
            <path fill="currentColor" d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/>
          </svg>
          <h2 class="text-2xl font-bold text-gray-900 mb-2">Aucune commande</h2>
          <p class="text-gray-600 mb-6">Vous n'avez pas encore passé de commande</p>
          <RouterLink 
            to="/cocktails" 
            class="inline-block bg-yellow-400 text-gray-900 font-bold px-6 py-3 rounded-lg hover:bg-yellow-500 transition-colors duration-200"
          >
            Commander maintenant
          </RouterLink>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}
</style> 