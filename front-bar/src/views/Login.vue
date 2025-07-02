<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '../api'

const router = useRouter()
const email = ref('')
const password = ref('')
const error = ref('')

const handleSubmit = async () => {
  error.value = ''
  if (!email.value) {
    error.value = "L'email est obligatoire."
    return
  }
  if (!password.value) {
    error.value = "Le mot de passe est obligatoire."
    return
  }
  try {
    await login(email.value, password.value)
    router.push('/cocktails')
  } catch (e) {
    if (e.response && e.response.data && e.response.data.message) {
      error.value = e.response.data.message
    } else {
      error.value = "Erreur réseau ou serveur."
    }
  }
}
</script>

<template>
  <div class="flex flex-col items-center justify-center min-h-screen">
    <div class="w-full max-w-md px-4">
      <h2 class="text-2xl font-bold mb-6 text-center">Connexion</h2>
      <form @submit.prevent="handleSubmit" class="flex flex-col gap-4">
        <div>
          <label class="block text-gray-700 text-sm font-bold mb-2" for="email">
            Email
          </label>
          <input
            v-model="email"
            id="email"
            type="email"
            class="w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none rounded shadow-inner bg-gray-200"
            required
          />
        </div>
        <div>
          <label class="block text-gray-700 text-sm font-bold mb-2" for="password">
            Mot de passe
          </label>
          <input
            v-model="password"
            id="password"
            type="password"
            class="w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none rounded shadow-inner bg-gray-200"
            required
          />
        </div>
        <div v-if="error" class="text-red-500 text-sm text-center">
          {{ error }}
        </div>
        <button
          type="submit"
          class="bg-primary hover:bg-secondary/80 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
        >
          Se connecter
        </button>
      </form>
      <div class="mt-4 text-center">
        <button
          class="text-secondary hover:underline text-sm"
          @click="router.push('/register')"
        >
          Pas encore de compte ? Inscrivez-vous
        </button>
      </div>
    </div>
  </div>
</template>
