import { createRouter, createWebHistory } from 'vue-router'
import Check from '../views/Check.vue'
import Cocktails from '../views/Cocktails.vue'
import Panier from '../views/Panier.vue'
import Logout from '../views/Logout.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/cocktails', component: Cocktails },
    { path: '/check', component: Check },
    { path: '/panier', component: Panier },
    { path: '/logout', component: Logout },
    { path: '/login', component: Login },
    { path: '/register', component: Register }
  ],
  scrollBehavior() {
    return { top: 0 } // scroll en haut à chaque navigation
  }
})

export default router