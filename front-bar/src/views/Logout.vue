
<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

onMounted(() => {
  // Suppression du token ou des infos de session (exemple localStorage)
  localStorage.removeItem('token')
  // Redirection vers la page de connexion ou d'accueil
  router.push('/login')
})
</script>

<template>
  <div class="flex flex-col items-center justify-center h-screen">
    <h1 class="text-2xl font-bold mb-4">Déconnexion...</h1>
    <p>Vous allez être redirigé.</p>
  </div>
</template>
