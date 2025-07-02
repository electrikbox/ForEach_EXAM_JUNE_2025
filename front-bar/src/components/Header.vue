<script setup>
import { useRoute } from 'vue-router'
import { onMounted } from 'vue'
import { useUserStore } from '../stores/userStore'
import { storeToRefs } from 'pinia'

const cocktailLink = "/cocktails"
const checkLink = "/check"
const panierLink = "/panier"
const logoutLink = "/logout"
const adminLink = "/admin"

const route = useRoute()

const isActive = (path) => route.path === path

const userStore = useUserStore()
const { user } = storeToRefs(userStore)

onMounted(async () => {
  if (!user.value) await userStore.fetchUser()
})

const isAdmin = () => {
  return user.value?.roles?.includes('ROLE_Barmaker')
}
</script>




<template>
  <nav class="fixed bottom-0 left-0 right-0 w-full z-50 bg-white p-2 overflow-x-hidden">
    <div class="flex flex-row justify-between items-center w-full max-w-md mx-auto">
      <RouterLink
        :to="cocktailLink"
        :class="['dock-label text-sm', { 'dock-active': isActive(cocktailLink) } ]"
      >
        <svg class="size-[1.2em] mx-auto" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><g fill="currentColor" stroke-linejoin="miter" stroke-linecap="butt"><polyline points="1 11 12 2 23 11" fill="none" stroke="currentColor" stroke-miterlimit="10" stroke-width="2"></polyline><path d="m5,13v7c0,1.105.895,2,2,2h10c1.105,0,2-.895,2-2v-7" fill="none" stroke="currentColor" stroke-linecap="square" stroke-miterlimit="10" stroke-width="2"></path><line x1="12" y1="22" x2="12" y2="18" fill="none" stroke="currentColor" stroke-linecap="square" stroke-miterlimit="10" stroke-width="2"></line></g></svg>
        Cocktails
      </RouterLink>
      <RouterLink
        :to="checkLink"
        :class="['dock-label text-sm', { 'dock-active': isActive(checkLink) } ]"
      >
        <svg class="size-[1.2em] mx-auto" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><g fill="currentColor" stroke-linejoin="miter" stroke-linecap="butt"><polyline points="3 14 9 14 9 17 15 17 15 14 21 14" fill="none" stroke="currentColor" stroke-miterlimit="10" stroke-width="2"></polyline><rect x="3" y="3" width="18" height="18" rx="2" ry="2" fill="none" stroke="currentColor" stroke-linecap="square" stroke-miterlimit="10" stroke-width="2"></rect></g></svg>
        Check
      </RouterLink>
      <RouterLink
        :to="panierLink"
        :class="['dock-label text-sm', { 'dock-active': isActive(panierLink) } ]"
      >
        <svg class="size-[1.2em] mx-auto" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><g fill="currentColor" stroke-linejoin="miter" stroke-linecap="butt"><circle cx="12" cy="12" r="3" fill="none" stroke="currentColor" stroke-linecap="square" stroke-miterlimit="10" stroke-width="2"></circle><path d="m22,13.25v-2.5l-2.318-.966c-.167-.581-.395-1.135-.682-1.654l.954-2.318-1.768-1.768-2.318.954c-.518-.287-1.073-.515-1.654-.682l-.966-2.318h-2.5l-.966,2.318c-.581.167-1.135.395-1.654.682l-2.318-.954-1.768,1.768.954,2.318c-.287.518-.515,1.073-.682,1.654l-2.318.966v2.5l2.318.966c.167.581.395,1.135.682,1.654l-.954,2.318,1.768,1.768,2.318-.954c.518.287,1.073.515,1.654.682l.966,2.318h2.5l.966-2.318c.581-.167,1.135-.395,1.654-.682l2.318.954,1.768-1.768-.954-2.318c.287-.518.515-1.073.682-1.654l2.318-.966Z" fill="none" stroke="currentColor" stroke-linecap="square" stroke-miterlimit="10" stroke-width="2"></path></g></svg>
        Panier
      </RouterLink>
      <RouterLink
        :to="logoutLink"
        :class="['dock-label text-sm', { 'dock-active': isActive(logoutLink) } ]"
      >
        <svg class="size-[1.2em] mx-auto" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><g fill="currentColor" stroke-linejoin="miter" stroke-linecap="butt"><circle cx="12" cy="12" r="3" fill="none" stroke="currentColor" stroke-linecap="square" stroke-miterlimit="10" stroke-width="2"></circle><path d="m22,13.25v-2.5l-2.318-.966c-.167-.581-.395-1.135-.682-1.654l.954-2.318-1.768-1.768-2.318.954c-.518-.287-1.073-.515-1.654-.682l-.966-2.318h-2.5l-.966,2.318c-.581.167-1.135.395-1.654.682l-2.318-.954-1.768,1.768.954,2.318c-.287.518-.515,1.073-.682,1.654l-2.318.966v2.5l2.318.966c.167.581.395,1.135.682,1.654l-.954,2.318,1.768,1.768,2.318-.954c.518.287,1.073.515,1.654.682l.966,2.318h2.5l.966-2.318c.581-.167,1.135-.395,1.654-.682l2.318.954,1.768-1.768-.954-2.318c.287-.518.515-1.073.682-1.654l2.318-.966Z" fill="none" stroke="currentColor" stroke-linecap="square" stroke-miterlimit="10" stroke-width="2"></path></g></svg>
        Déconnexion
      </RouterLink>
      <RouterLink
        v-if="isAdmin()"
        :to="adminLink"
        :class="['dock-label text-sm', { 'dock-active': isActive(adminLink) } ]"
      >
        <svg class="size-[1.2em] mx-auto" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24"><g fill="currentColor"><rect x="4" y="4" width="16" height="16" rx="2"/><path d="M8 12h8M12 8v8" stroke="#fff" stroke-width="2" stroke-linecap="round"/></g></svg>
        Admin
      </RouterLink>
    </div>
  </nav>
</template>
