import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'
import Inicio from '@/views/Inicio.vue'
import Perfil from '@/views/Perfil.vue'
import EditarPerfilCard from '@/components/perfil/EditarPerfilCard.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Inicio',
      component: Inicio,
    },
    {
      path: '/login',
      name: 'Login',
      component: Login,
    },
    {
      path: '/perfil',
      name: 'Perfil',
      component: Perfil,
      children: [
        {
          path: "editar",
          name: 'EditarPerfil',
          component: EditarPerfilCard,
        }
      ]
    },
    {
      path: '/test',
      name: 'Test',
      component: EditarPerfilCard
    }
  ]
})

export default router
