<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '../api'

const router = useRouter()
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const error = ref('')
const success = ref('')

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
  if (!confirmPassword.value) {
    error.value = "La confirmation du mot de passe est obligatoire."
    return
  }
  if (password.value !== confirmPassword.value) {
    error.value = "Les mots de passe ne correspondent pas."
    return
  }
  try {
    await register(email.value, password.value)
    success.value = "Inscription réussie, vous pouvez vous connecter."
    setTimeout(() => {
      router.push('/login')
    }, 1500)
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
      <h2 class="text-2xl font-bold mb-6 text-center">Inscription</h2>
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
        <div>
          <label class="block text-gray-700 text-sm font-bold mb-2" for="confirmPassword">
            Confirmer le mot de passe
          </label>
          <input
            v-model="confirmPassword"
            id="confirmPassword"
            type="password"
            class="w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none rounded shadow-inner bg-gray-200"
            required
          />
        </div>
        <div v-if="error" class="text-red-500 text-sm text-center">
          {{ error }}
        </div>
        <div v-if="success" class="text-green-600 text-sm text-center">
          {{ success }}
        </div>
        <button
          type="submit"
          class="bg-primary hover:bg-secondary/80 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline"
        >
          S'inscrire
        </button>
      </form>
      <div class="mt-4 text-center">
        <button
          class="text-secondary hover:underline text-sm"
          @click="router.push('/login')"
        >
          Déjà un compte ? Connectez-vous
        </button>
      </div>
    </div>
  </div>
</template> 