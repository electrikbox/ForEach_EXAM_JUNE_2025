import { defineStore } from 'pinia'
import { getCocktails } from '@/api'

export const useCocktailStore = defineStore('cocktail', {
  state: () => ({
    cocktails: [] as any[],
    loading: false as boolean,
    error: null as string | null
  }),
  actions: {
    async fetchCocktails() {
      this.loading = true
      this.error = null
      try {
        const res = await getCocktails()
        this.cocktails = res.data
      } catch (e: any) {
        this.error = e.message || 'Erreur lors du chargement des cocktails'
      } finally {
        this.loading = false
      }
    }
  }
}) 