
<script setup>
import { logout } from '@/api'
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

onMounted(async() => {
  await logout();
  router.push('/login')
})
</script>
