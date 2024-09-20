import { createRouter, createWebHistory } from 'vue-router'
import LoginForm from '@/components/login/LoginForm.vue'
import EditarPerfilForm from '@/components/perfil/EditarPerfilForm.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginForm
    },
    {
      path: '/editar-perfil',
      name: 'EditarPerfil',
      component: EditarPerfilForm
    }
  ]
})

export default router
