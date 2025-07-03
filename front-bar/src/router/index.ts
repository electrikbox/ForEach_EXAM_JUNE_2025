import { createRouter, createWebHistory } from 'vue-router'
import MesCommandes from '../views/MesCommandes.vue'
import Cocktails from '../views/Cocktails.vue'
import Panier from '../views/Panier.vue'
import Logout from '../views/Logout.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import { useUserStore } from '@/stores/userStore'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/cocktails', component: Cocktails, meta: { requiresAuth: true } },
    { path: '/mes-commandes', component: MesCommandes, meta: { requiresAuth: true } },
    { path: '/panier', component: Panier, meta: { requiresAuth: true } },
    { path: '/logout', component: Logout, meta: { requiresAuth: true } },
    { path: '/login', component: Login },
    { path: '/register', component: Register }
  ],
  scrollBehavior() {
    return { top: 0 } // scroll en haut à chaque navigation
  }
})

export default router