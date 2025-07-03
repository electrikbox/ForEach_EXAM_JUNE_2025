<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/userStore'
import { getAllCommandes, updateCommandeStatus } from '../api'

interface LigneCommande {
  idLigneCommande: number;
  nomCocktail: string;
  nomTaille: string;
  quantite: number;
  prix: number;
}

interface Utilisateur {
  id: number;
  email: string;
  role: string;
}

interface Commande {
  idCommande: number;
  dateCommande: string;
  statutCommande: string;
  utilisateur?: Utilisateur;
  lignes: LigneCommande[];
  total: number;
}

const userStore = useUserStore()
const commandes = ref<Commande[]>([])
const loading = ref(true)
const error = ref('')

const fetchCommandes = async () => {
  try {
    loading.value = true
    const response = await getAllCommandes()
    console.log('Réponse API:', response.data)
    commandes.value = response.data.map((commande: any) => ({
      ...commande,
      utilisateur: commande.utilisateur || { email: 'Client inconnu' }
    }))
  } catch (err: any) {
    error.value = 'Erreur lors du chargement des commandes'
    console.error('Erreur détaillée:', err.response?.data || err)
  } finally {
    loading.value = false
  }
}

const updateStatut = async (commandeId: number, nouveauStatut: string) => {
  try {
    await updateCommandeStatus(commandeId, nouveauStatut)
    await fetchCommandes()
  } catch (err: any) {
    alert('Erreur lors de la mise à jour du statut')
    console.error('Erreur détaillée:', err.response?.data || err)
  }
}

onMounted(fetchCommandes)
</script>

<template>
  <div class="py-8 px-4 sm:px-6 lg:px-8">
    <div class="max-w-6xl mx-auto">
      <h1 class="text-4xl font-bold text-gray-900 mb-8">Administration des Commandes</h1>

      <div v-if="loading" class="text-center py-12">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-yellow-400 mx-auto"></div>
      </div>

      <div v-else-if="error" class="bg-red-50 border border-red-200 rounded-lg p-4 text-red-800">
        {{ error }}
      </div>

      <div v-else-if="commandes.length === 0" class="text-center py-12 text-gray-500">
        Aucune commande à afficher
      </div>

      <div v-else class="space-y-6">
        <div v-for="commande in commandes" :key="commande.idCommande" 
             class="bg-white rounded-xl shadow-sm p-6">
          <div class="flex justify-between items-start mb-4">
            <div>
              <h3 class="text-lg font-semibold">
                Commande #{{ commande.idCommande }}
              </h3>
              <p class="text-sm text-gray-500">
                {{ new Date(commande.dateCommande).toLocaleString() }}
              </p>
              <p class="text-sm text-gray-600 mt-1">
                Client: {{ commande.utilisateur?.email || 'Client inconnu' }}
              </p>
            </div>
            <div class="flex items-center space-x-4">
              <span :class="{
                'px-3 py-1 rounded-full text-sm font-medium': true,
                'bg-yellow-100 text-yellow-800': commande.statutCommande === 'Commandée',
                'bg-blue-100 text-blue-800': commande.statutCommande === 'en cours de préparation',
                'bg-gray-100 text-gray-800': commande.statutCommande === 'Terminée'
              }">
                {{ commande.statutCommande }}
              </span>
              <select 
                :value="commande.statutCommande"
                @change="(e) => updateStatut(commande.idCommande, (e.target as HTMLSelectElement).value)"
                class="border border-gray-300 rounded-lg px-3 py-1 text-sm"
              >
                <option value="Commandée">Commandée</option>
                <option value="en cours de préparation">En cours de préparation</option>
                <option value="Terminée">Terminée</option>
              </select>
            </div>
          </div>

          <div class="border-t border-gray-100 pt-4 mt-4">
            <ul class="divide-y divide-gray-100">
              <li v-for="ligne in commande.lignes" :key="ligne.idLigneCommande" class="py-2">
                <div class="flex justify-between">
                  <div>
                    <span class="font-medium">{{ ligne.nomCocktail }}</span>
                    <span class="text-gray-500"> - {{ ligne.nomTaille }}</span>
                  </div>
                  <span class="text-gray-600">× {{ ligne.quantite }}</span>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</template> 