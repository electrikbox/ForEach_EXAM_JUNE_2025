<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import Header from './components/Header.vue'

const route = useRoute()
const transitionName = ref('slide')

function handleResize() {
  transitionName.value = window.innerWidth < 640 ? '' : 'slide'
}

onMounted(() => {
  handleResize()
  window.addEventListener('resize', handleResize)
})
onUnmounted(() => {
  window.removeEventListener('resize', handleResize)
})
</script>

<template>
  <Header v-if="route.path !== '/login' && route.path !== '/register'" />
  <main class="pb-20">
    <router-view v-slot="{ Component }">
      <transition :name="transitionName" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
  </main>
</template>

<style>
/* transition fade (non utilisée ici mais tu peux la remettre) */
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.05s ease-in-out;
}

/* transition slide */
.slide-enter-from,
.slide-leave-to {
  opacity: 0;
  transform: translateX(10px);
}
.slide-enter-active,
.slide-leave-active {
  transition: 0.05s ease-out;
}
</style>
