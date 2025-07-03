import { createRouter, createWebHistory } from 'vue-router'
import MesCommandes from '../views/MesCommandes.vue'
import Cocktails from '../views/Cocktails.vue'
import Panier from '../views/Panier.vue'
import Logout from '../views/Logout.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import AdminCommandes from '../views/AdminCommandes.vue'
import { useUserStore } from '@/stores/userStore'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/cocktails', component: Cocktails, meta: { requiresAuth: true } },
    { path: '/mes-commandes', component: MesCommandes, meta: { requiresAuth: true } },
    { path: '/panier', component: Panier, meta: { requiresAuth: true } },
    { path: '/logout', component: Logout, meta: { requiresAuth: true } },
    { path: '/login', component: Login },
    { path: '/register', component: Register },
    { 
      path: '/admin/commandes', 
      component: AdminCommandes, 
      meta: { 
        requiresAuth: true,
        requiresAdmin: true 
      } 
    }
  ],
  scrollBehavior() {
    return { top: 0 } // scroll en haut à chaque navigation
  }
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.isAuthenticated) {
    next('/login')
  } else if (to.meta.requiresAdmin && !userStore.user?.roles.includes('ROLE_Barmaker')) {
    next('/cocktails')
  } else {
    next()
  }
})

export default router