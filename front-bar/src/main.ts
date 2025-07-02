import './style.css'
import ToastPlugin from 'vue-toast-notification';
import 'vue-toast-notification/dist/theme-bootstrap.css';
import { createPinia } from 'pinia'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
 
const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ToastPlugin);
app.mount('#app')