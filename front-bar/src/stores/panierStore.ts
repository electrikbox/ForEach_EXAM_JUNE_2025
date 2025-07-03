import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface LignePanier {
  id: string // unique pour identifier la ligne
  cocktailId: number
  nomCocktail: string
  tailleId: number
  nomTaille: string
  prix: number
  quantite: number
}

export const usePanierStore = defineStore('panier', () => {
  const lignes = ref<LignePanier[]>([])

  // Getters
  const total = computed(() => {
    return lignes.value.reduce((sum, ligne) => sum + (ligne.prix * ligne.quantite), 0)
  })

  const nombreArticles = computed(() => {
    return lignes.value.reduce((sum, ligne) => sum + ligne.quantite, 0)
  })

  // Actions
  const ajouterAuPanier = (cocktail: any, taille: any, quantite: number) => {
    const ligneId = `${cocktail.idCocktail}-${taille.idTaille}`
    
    const ligneExistante = lignes.value.find(l => l.id === ligneId)
    
    if (ligneExistante) {
      // Si la même taille existe déjà, on ajoute la quantité
      ligneExistante.quantite += quantite
    } else {
      // Sinon on crée une nouvelle ligne
      lignes.value.push({
        id: ligneId,
        cocktailId: cocktail.idCocktail,
        nomCocktail: cocktail.nomCocktail,
        tailleId: taille.idTaille,
        nomTaille: taille.nomTaille,
        prix: taille.prix,
        quantite: quantite
      })
    }
    
    // Sauvegarder dans localStorage
    sauvegarderPanier()
  }

  const modifierQuantite = (ligneId: string, nouvelleQuantite: number) => {
    const ligne = lignes.value.find(l => l.id === ligneId)
    if (ligne) {
      if (nouvelleQuantite <= 0) {
        supprimerLigne(ligneId)
      } else {
        ligne.quantite = nouvelleQuantite
        sauvegarderPanier()
      }
    }
  }

  const supprimerLigne = (ligneId: string) => {
    lignes.value = lignes.value.filter(l => l.id !== ligneId)
    sauvegarderPanier()
  }

  const viderPanier = () => {
    lignes.value = []
    sauvegarderPanier()
  }

  // Persistance localStorage
  const sauvegarderPanier = () => {
    localStorage.setItem('panier', JSON.stringify(lignes.value))
  }

  const chargerPanier = () => {
    const panierSauvegarde = localStorage.getItem('panier')
    if (panierSauvegarde) {
      lignes.value = JSON.parse(panierSauvegarde)
    }
  }

  // Charger le panier au démarrage
  chargerPanier()

  return {
    lignes,
    total,
    nombreArticles,
    ajouterAuPanier,
    modifierQuantite,
    supprimerLigne,
    viderPanier
  }
}) 