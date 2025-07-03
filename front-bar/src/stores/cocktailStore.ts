// import { defineStore } from 'pinia'
// import { getCocktails } from '@/api'

// export const useCocktailStore = defineStore('cocktail', {
//   state: () => ({
//     cocktails: [] as any[],
//     loading: false as boolean,
//     error: null as string | null
//   }),
//   actions: {
//     async fetchCocktails() {
//       this.loading = true
//       this.error = null
//       try {
//         const res = await getCocktails()
//         this.cocktails = res.data
//       } catch (e: any) {
//         this.error = e.message || 'Erreur lors du chargement des cocktails'
//       } finally {
//         this.loading = false
//       }
//     }
//   }
// }) 


// stores/cocktailStore.js
import { defineStore } from 'pinia'
// Assurez-vous d'importer getCategories si vous l'avez dans votre fichier api.js
import { getCocktails, getCategories } from '@/api' // <--- Assurez-vous d'avoir getCategories

export const useCocktailStore = defineStore('cocktail', {
  state: () => ({
    cocktails: [] as any[],
    categories: [] as any[], // <--- Ajouté
    selectedCategory: null as string | null, // <--- Ajouté, initialisé à null pour "Toutes"
    isLoading: false as boolean, // <--- Renommé de 'loading' à 'isLoading'
    error: null as string | null
  }),
  getters: { // <--- Ajouté la section getters
    filteredCocktails: (state) => {
      if (!state.selectedCategory) {
        return state.cocktails
      }
      return state.cocktails.filter(
        (cocktail) => cocktail.categorie?.idCategorie === state.selectedCategory
      )
    },
    // La propriété 'loading' est devenue 'isLoading' et sera accessible via store.isLoading
    // Ou vous pouvez définir un getter pour elle si vous préférez, mais ce n'est pas nécessaire
  },
  actions: {
    async fetchCocktails() {
      this.isLoading = true // Utilisation de isLoading
      this.error = null
      try {
        const res = await getCocktails()
        this.cocktails = res.data
      } catch (e: any) {
        this.error = e.message || 'Erreur lors du chargement des cocktails'
      } finally {
        this.isLoading = false
      }
    },
    async fetchCategories() { // <--- Ajouté
      try {
        const res = await getCategories() // Assurez-vous que getCategories est implémentée
        this.categories = res.data
      } catch (e: any) {
        console.error('Erreur lors du chargement des catégories:', e.message)
      }
    },
    async fetchAllData() { // <--- Nouvelle action pour tout charger
        this.isLoading = true;
        this.error = null;
        try {
            await Promise.all([
                this.fetchCocktails(),
                this.fetchCategories()
            ]);
        } catch (e: any) {
            this.error = e.message || 'Erreur lors du chargement initial';
            console.error(e);
        } finally {
            this.isLoading = false;
        }
    },
    setSelectedCategory(categoryId: string | null) { // <--- Nouvelle action pour changer la catégorie
      this.selectedCategory = categoryId
    }
  }
})