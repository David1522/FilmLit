import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'

import Perfil from '@/views/Perfil.vue'
import EditarPerfilCard from '@/components/perfil/EditarPerfilCard.vue'

import Publicaciones from '@/views/Publicaciones.vue';
import CrearPublicacion from '@/components/publicaciones/CrearPublicacion.vue';
import DetallesPublicacion from '@/components/publicaciones/DetallesPublicacion.vue'

import Noticias from '@/views/Noticias.vue'

import Biblioteca from '@/views/Biblioteca.vue'
import Principal from '@/components/biblioteca/Explorar.vue'
import Peliculas from '@/components/biblioteca/Peliculas.vue'
import Series from '@/components/biblioteca/Series.vue'
import Libros from '@/components/biblioteca/Libros.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login,
    },
    {
      path: '/perfil',
      name: 'Perfil',
      component: Perfil,
      children: [
        {
          path: 'editar',
          name: 'EditarPerfil',
          component: EditarPerfilCard,
        }
      ],
    },
    {
      path: '/publicaciones',
      name: 'Publicaciones',
      component: Publicaciones,
      children: [
        {
          path: 'crear',
          name: 'CrearPublicacion',
          component: CrearPublicacion,
        },
        {
          path: 'detalles/:id',
          name: 'DetallesPublicacion',
          component: DetallesPublicacion,
        }
      ],
    },
    {
      path: '/noticias',
      name: 'Noticias',
      component: Noticias,
    },
    {
      path: '/biblioteca',
      name: 'Biblioteca',
      component: Biblioteca,
      children: [
          {
              path: 'peliculas',
              name: 'Peliculas',
              component: Peliculas,
          },
          {
              path: 'series',
              name: 'Series',
              component: Series,
          },{
            path: 'libros',
            name: 'Libros',
            component: Libros,
        },
      ],
  }
  ]
})

export default router
