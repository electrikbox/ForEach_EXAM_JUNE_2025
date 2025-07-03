import { defineStore } from 'pinia'
import { getUser } from '../api'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null as null | { id: number, username: string, roles: string[] }
  }),
  getters: {
    isAuthenticated: (state) => state.user !== null
  },
  actions: {
    async fetchUser() {
      try {
        const res = await getUser()
        console.log(res)
        this.user = res.data
      } catch (e: any) {
        console.error('Erreur fetchUser', e)
        this.user = null
        if (e.response?.status === 401) {
          const currentPath = window.location.pathname
          if (currentPath !== '/login' && currentPath !== '/register') {
            window.location.href = '/login'
          }
        }
      }
    },
    setUser(user: any) {
      this.user = user
    }
  }
}) 