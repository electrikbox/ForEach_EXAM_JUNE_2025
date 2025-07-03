<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { usePanierStore } from '../stores/panierStore'
import { useUserStore } from '../stores/userStore'
import { creerCommande } from '../api'

const router = useRouter()
const panierStore = usePanierStore()
const userStore = useUserStore()

const validationEnCours = ref(false)

const modifierQuantite = (ligneId: string, nouvelleQuantite: number) => {
  panierStore.modifierQuantite(ligneId, nouvelleQuantite)
}

const supprimerArticle = (ligneId: string) => {
  if (confirm('Êtes-vous sûr de vouloir supprimer cet article ?')) {
    panierStore.supprimerLigne(ligneId)
  }
}

const viderPanier = () => {
  if (confirm('Êtes-vous sûr de vouloir vider votre panier ?')) {
    panierStore.viderPanier()
  }
}

const validerCommande = async () => {
  if (!userStore.user) {
    alert('Vous devez être connecté pour valider une commande')
    return
  }

  console.log('Utilisateur connecté:', userStore.user)
  console.log('Rôles de l\'utilisateur:', userStore.user.roles)

  validationEnCours.value = true

  const commandeData = {
    utilisateurId: userStore.user.id,
    lignes: panierStore.lignes.map(ligne => ({
      cocktailId: ligne.cocktailId,
      tailleId: ligne.tailleId,
      quantite: ligne.quantite,
      statutCocktailPreparation: "Préparation des Ingrédients"
    }))
  }
  
  console.log('Données de la commande:', commandeData)
  
  try {
    await creerCommande(commandeData);
    
    alert('Commande validée avec succès !')
    panierStore.viderPanier()
    router.push('/cocktails')
    
  } catch (error: any) {
    alert('Erreur lors de la validation de la commande')
    console.error('Erreur validation commande:', error)
    console.error('Détails de l\'erreur:', error.response?.data)
  } finally {
    validationEnCours.value = false
  }
}
</script>

<template>
  <div class="min-h-screen bg-gray-50 py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-6xl mx-auto"> 
      <div class="flex justify-between items-center mb-8 pb-4 border-b border-gray-200">
        <h1 class="text-4xl font-bold text-gray-900 text-center flex-grow">Panier</h1>
        <button 
          v-if="panierStore.lignes.length > 0"
          @click="viderPanier" 
          class="text-red-600 hover:text-red-800 text-sm font-medium transition-colors duration-200"
        >
          Vider le panier
        </button>
      </div>

      <div v-if="panierStore.lignes.length === 0" class="flex flex-col items-center justify-center py-20 text-gray-500">
        <svg class="w-24 h-24 text-gray-300 mb-6" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
          <g fill="currentColor" stroke-linejoin="miter" stroke-linecap="butt">
            <circle cx="12" cy="12" r="3" fill="none" stroke="currentColor" stroke-linecap="square" stroke-miterlimit="10" stroke-width="2"></circle>
            <path d="m22,13.25v-2.5l-2.318-.966c-.167-.581-.395-1.135-.682-1.654l.954-2.318-1.768-1.768-2.318.954c-.518-.287-1.073-.515-1.654-.682l-.966-2.318h-2.5l-.966,2.318c-.581.167-1.135.395-1.654.682l-2.318-.954-1.768,1.768.954,2.318c-.287.518-.515,1.073-.682,1.654l-2.318.966v2.5l2.318.966c.167.581.395,1.135.682,1.654l-.954,2.318,1.768,1.768,2.318-.954c.518.287,1.073.515,1.654.682l.966,2.318h2.5l.966-2.318c.581-.167,1.135-.395,1.654-.682l2.318-.966Z" fill="none" stroke="currentColor" stroke-linecap="square" stroke-miterlimit="10" stroke-width="2"></path>
          </g>
        </svg>
        <h2 class="text-2xl font-semibold mb-2 text-gray-700">Votre panier est vide</h2>
        <p class="mb-6">Ajoutez des cocktails pour commencer vos achats</p>
        <RouterLink to="/cocktails" class="bg-yellow-400 text-gray-900 font-bold py-3 px-6 rounded-lg hover:bg-yellow-500 transition-colors duration-200">
          Voir les cocktails
        </RouterLink>
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div class="md:col-span-2 space-y-4">
          <div 
            v-for="ligne in panierStore.lignes" 
            :key="ligne.id" 
            class="flex flex-col md:flex-row items-start md:items-center bg-white p-3 rounded-xl shadow-sm relative"
          >
            <div class="flex items-center w-full md:w-auto mb-4 md:mb-0">
                <img 
                :src="'https://via.placeholder.com/64?text=Cocktail'" 
                alt="Image du cocktail" 
                class="w-16 h-16 rounded-lg object-cover mr-4"
                >
                
                <div class="flex-grow">
                    <h3 class="text-lg font-semibold text-gray-900">{{ ligne.nomCocktail }}</h3>
                    <p class="text-gray-600 text-sm">{{ ligne.nomTaille }}</p>
                    <p class="text-gray-800 font-medium">{{ ligne.prix.toFixed(2) }}€ / unité</p>
                </div>
            </div>
            
            <div class="flex items-center justify-between w-full md:w-auto md:ml-auto">
                <div class="flex items-center space-x-2">
                    <button 
                        @click="modifierQuantite(ligne.id, ligne.quantite - 1)"
                        :disabled="ligne.quantite <= 1"
                        class="w-8 h-8 flex items-center justify-center text-gray-700 text-lg rounded-full border border-gray-300 bg-white hover:bg-gray-100 disabled:opacity-40 disabled:cursor-not-allowed transition-colors duration-200"
                    >
                        -
                    </button>
                    <span class="text-base font-semibold text-gray-800">{{ ligne.quantite }}</span>
                    <button 
                        @click="modifierQuantite(ligne.id, ligne.quantite + 1)"
                        class="w-8 h-8 flex items-center justify-center text-gray-700 text-lg rounded-full border border-gray-300 bg-white hover:bg-gray-100 transition-colors duration-200"
                    >
                        +
                    </button>
                </div>
                
                <button 
                @click="supprimerArticle(ligne.id)"
                class="ml-4 text-red-500 hover:text-red-700 p-2 rounded-full hover:bg-red-50 transition-colors duration-200"
                title="Supprimer l'article"
                >
                <svg class="w-5 h-5" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
                    <path fill="currentColor" d="M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/>
                </svg>
                </button>
            </div>
          </div>
        </div>

        <div class="md:col-span-1 bg-white p-6 rounded-xl shadow-sm h-fit">
          <h3 class="text-xl font-semibold text-gray-900 mb-6 pb-3 border-b border-gray-200">Résumé de la commande</h3>
          
          <div class="space-y-3 mb-8">
            <div class="flex justify-between items-center text-gray-700">
              <span>Nombre d'articles :</span>
              <span class="font-medium">{{ panierStore.nombreArticles }}</span>
            </div>
            <div class="flex justify-between items-center text-xl font-bold text-gray-900 pt-3 border-t border-gray-100">
              <span>Total :</span>
              <span class="text-yellow-500">{{ panierStore.total.toFixed(2) }}€</span>
            </div>
          </div>
          
          <button 
            @click="validerCommande"
            :disabled="validationEnCours || panierStore.lignes.length === 0"
            class="w-full p-4 bg-yellow-400 text-gray-900 font-bold text-lg rounded-xl shadow-md hover:bg-yellow-500 transition-colors duration-200 disabled:bg-gray-300 disabled:text-gray-600 disabled:cursor-not-allowed"
          >
            <span v-if="validationEnCours">Validation en cours...</span>
            <span v-else>Valider ma commande</span>
          </button>
        </div>
      </div>
    </div>
  </div>

  </template>
