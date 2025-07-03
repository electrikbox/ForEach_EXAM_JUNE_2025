import { defineStore } from 'pinia'
import { getUser } from '../api'

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null as null | { id: number, username: string, roles: string[] }
  }),
  actions: {
    async fetchUser() {
      try {
        const res = await getUser()
        console.log(res)
        this.user = res.data
      } catch (e: any) {
        console.error('Erreur fetchUser', e)
        this.user = null
        if (
          e.response &&
          e.response.status === 401 &&
          window.location.pathname !== '/login' &&
          window.location.pathname !== '/register'
        ) {
          window.location.href = '/login'
        }
      }
    },
    setUser(user: any) {
      this.user = user
    }
  }
}) 