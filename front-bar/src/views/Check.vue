<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useUserStore } from '../stores/userStore'
// import { api } from '../api'

interface LigneCommande {
  idLigneCommande: number
  cocktail: {
    idCocktail: number
    nomCocktail: string
  }
  taille: {
    idTaille: number
    nomTaille: string
  }
  quantite: number
  prix: number
}

interface Commande {
  idCommande: number
  dateCommande: string
  statut: string
  total: number
  lignes: LigneCommande[]
}

const userStore = useUserStore()
const commandes = ref<Commande[]>([])
const chargementEnCours = ref(false)

const chargerCommandes = async () => {
  if (!userStore.user) return
  
  chargementEnCours.value = true
  
  try {
    // TODO: Remplacer par l'appel API réel
    // const response = await api.get(`/api/commandes/utilisateur/${userStore.user.id}`)
    // commandes.value = response.data
    
    // Simulation de données
    await new Promise(resolve => setTimeout(resolve, 1000))
    commandes.value = [
      {
        idCommande: 1,
        dateCommande: '2024-01-15T14:30:00',
        statut: 'EN_PREPARATION',
        total: 24.50,
        lignes: [
          {
            idLigneCommande: 1,
            cocktail: { idCocktail: 1, nomCocktail: 'Mojito' },
            taille: { idTaille: 2, nomTaille: 'Grand' },
            quantite: 2,
            prix: 12.25
          }
        ]
      },
      {
        idCommande: 2,
        dateCommande: '2024-01-10T19:15:00',
        statut: 'TERMINEE',
        total: 18.00,
        lignes: [
          {
            idLigneCommande: 2,
            cocktail: { idCocktail: 3, nomCocktail: 'Margarita' },
            taille: { idTaille: 1, nomTaille: 'Moyen' },
            quantite: 1,
            prix: 18.00
          }
        ]
      }
    ]
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
    'EN_ATTENTE': 'En attente',
    'EN_PREPARATION': 'En préparation',
    'PRETE': 'Prête',
    'TERMINEE': 'Terminée',
    'ANNULEE': 'Annulée'
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
  <div class="check-container">
    <div class="check-header">
      <h1 class="check-title">Mes Commandes</h1>
      <button 
        @click="chargerCommandes" 
        :disabled="chargementEnCours"
        class="btn-refresh"
      >
        <svg class="refresh-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
          <path fill="currentColor" d="M17.65 6.35A7.958 7.958 0 0012 4c-4.42 0-7.99 3.58-7.99 8s3.57 8 7.99 8c3.73 0 6.84-2.55 7.73-6h-2.08A5.99 5.99 0 0112 18c-3.31 0-6-2.69-6-6s2.69-6 6-6c1.66 0 3.14.69 4.22 1.78L13 11h7V4l-2.35 2.35z"/>
        </svg>
        Actualiser
      </button>
    </div>

    <!-- Chargement -->
    <div v-if="chargementEnCours" class="chargement">
      <div class="spinner"></div>
      <p>Chargement de vos commandes...</p>
    </div>

    <!-- Non connecté -->
    <div v-else-if="!userStore.user" class="non-connecte">
      <div class="non-connecte-content">
        <svg class="non-connecte-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
          <path fill="currentColor" d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm-2 15l-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/>
        </svg>
        <h2>Connexion requise</h2>
        <p>Vous devez être connecté pour voir vos commandes</p>
        <RouterLink to="/login" class="btn-login">
          Se connecter
        </RouterLink>
      </div>
    </div>

    <!-- Liste des commandes -->
    <div v-else-if="commandes.length > 0" class="commandes-list">
      <div 
        v-for="commande in commandes" 
        :key="commande.idCommande" 
        class="commande-card"
      >
        <div class="commande-header">
          <div class="commande-info">
            <h3 class="commande-numero">Commande #{{ commande.idCommande }}</h3>
            <p class="commande-date">{{ formaterDate(commande.dateCommande) }}</p>
          </div>
          <div class="commande-statut">
            <span :class="['statut-badge', `statut-${commande.statut.toLowerCase()}`]">
              {{ getStatutLabel(commande.statut) }}
            </span>
          </div>
        </div>

        <div class="commande-lignes">
          <div 
            v-for="ligne in commande.lignes" 
            :key="ligne.idLigneCommande" 
            class="ligne-commande"
          >
            <div class="ligne-info">
              <span class="ligne-nom">{{ ligne.cocktail.nomCocktail }}</span>
              <span class="ligne-taille">{{ ligne.taille.nomTaille }}</span>
            </div>
            <div class="ligne-details">
              <span class="ligne-quantite">{{ ligne.quantite }}x</span>
              <span class="ligne-prix">{{ (ligne.prix * ligne.quantite).toFixed(2) }}€</span>
            </div>
          </div>
        </div>

        <div class="commande-footer">
          <div class="commande-total">
            <span class="total-label">Total :</span>
            <span class="total-prix">{{ commande.total.toFixed(2) }}€</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Aucune commande -->
    <div v-else class="aucune-commande">
      <div class="aucune-commande-content">
        <svg class="aucune-commande-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24">
          <path fill="currentColor" d="M19 3H5c-1.1 0-2 .9-2 2v14c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V5c0-1.1-.9-2-2-2zm-5 14H7v-2h7v2zm3-4H7v-2h10v2zm0-4H7V7h10v2z"/>
        </svg>
        <h2>Aucune commande</h2>
        <p>Vous n'avez pas encore passé de commande</p>
        <RouterLink to="/cocktails" class="btn-commander">
          Commander maintenant
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<style scoped>
.check-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  min-height: calc(100vh - 100px);
}

.check-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 15px;
  border-bottom: 2px solid #f0f0f0;
}

.check-title {
  font-size: 2rem;
  font-weight: bold;
  color: #333;
  margin: 0;
}

.btn-refresh {
  display: flex;
  align-items: center;
  gap: 8px;
  background: #007bff;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-refresh:hover:not(:disabled) {
  background: #0056b3;
}

.btn-refresh:disabled {
  background: #6c757d;
  cursor: not-allowed;
}

.refresh-icon {
  width: 16px;
  height: 16px;
}

.chargement {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  color: #666;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.non-connecte, .aucune-commande {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 400px;
}

.non-connecte-content, .aucune-commande-content {
  text-align: center;
  color: #666;
}

.non-connecte-icon, .aucune-commande-icon {
  width: 80px;
  height: 80px;
  margin-bottom: 20px;
  color: #ccc;
}

.non-connecte-content h2, .aucune-commande-content h2 {
  font-size: 1.5rem;
  margin-bottom: 10px;
  color: #333;
}

.non-connecte-content p, .aucune-commande-content p {
  margin-bottom: 20px;
}

.btn-login, .btn-commander {
  background: #007bff;
  color: white;
  text-decoration: none;
  padding: 12px 24px;
  border-radius: 4px;
  display: inline-block;
}

.btn-login:hover, .btn-commander:hover {
  background: #0056b3;
}

.commandes-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.commande-card {
  background: white;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.commande-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.commande-numero {
  font-size: 1.2rem;
  font-weight: bold;
  margin: 0 0 5px 0;
  color: #333;
}

.commande-date {
  color: #666;
  margin: 0;
  font-size: 0.9rem;
}

.statut-badge {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: bold;
  text-transform: uppercase;
}

.statut-en_attente {
  background: #fff3cd;
  color: #856404;
}

.statut-en_preparation {
  background: #cce5ff;
  color: #004085;
}

.statut-prete {
  background: #d4edda;
  color: #155724;
}

.statut-terminee {
  background: #d1ecf1;
  color: #0c5460;
}

.statut-annulee {
  background: #f8d7da;
  color: #721c24;
}

.commande-lignes {
  margin-bottom: 15px;
}

.ligne-commande {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  border-bottom: 1px solid #f8f9fa;
}

.ligne-commande:last-child {
  border-bottom: none;
}

.ligne-info {
  flex: 1;
}

.ligne-nom {
  display: block;
  font-weight: bold;
  color: #333;
}

.ligne-taille {
  display: block;
  font-size: 0.9rem;
  color: #666;
}

.ligne-details {
  text-align: right;
}

.ligne-quantite {
  display: block;
  font-size: 0.9rem;
  color: #666;
}

.ligne-prix {
  display: block;
  font-weight: bold;
  color: #28a745;
}

.commande-footer {
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.commande-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.total-label {
  font-size: 1.1rem;
  font-weight: bold;
  color: #333;
}

.total-prix {
  font-size: 1.2rem;
  font-weight: bold;
  color: #28a745;
}

/* Responsive */
@media (max-width: 768px) {
  .check-header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
  }
  
  .commande-header {
    flex-direction: column;
    gap: 10px;
  }
  
  .ligne-commande {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
  
  .ligne-details {
    text-align: left;
  }
}
</style>
