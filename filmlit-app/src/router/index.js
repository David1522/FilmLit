import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Perfil from '@/views/Perfil.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: Login
    },
    {
      path: '/perfil',
      name: 'Perfil',
      component: Perfil
    }
  ]
})

export default router
