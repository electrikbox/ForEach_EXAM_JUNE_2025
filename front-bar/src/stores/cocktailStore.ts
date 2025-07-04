import { defineStore } from 'pinia'
import { getCocktails, getCategories } from '@/api'

export const useCocktailStore = defineStore('cocktail', {
  state: () => ({
    cocktails: [] as any[],
    categories: [] as any[],
    selectedCategory: null as string | null,
    isLoading: false as boolean,
    error: null as string | null
  }),
  getters: {
    filteredCocktails: (state) => {
      if (!state.selectedCategory) {
        return state.cocktails
      }
      return state.cocktails.filter(
        (cocktail) => cocktail.categorie?.idCategorie === state.selectedCategory
      )
    },
  },
  actions: {
    async fetchCocktails() {
      this.isLoading = true
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
    async fetchCategories() {
      try {
        const res = await getCategories()
        this.categories = res.data
      } catch (e: any) {
        console.error('Erreur lors du chargement des catégories:', e.message)
      }
    },
    async fetchAllData() {
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
    setSelectedCategory(categoryId: string | null) {
      this.selectedCategory = categoryId
    }
  }
})