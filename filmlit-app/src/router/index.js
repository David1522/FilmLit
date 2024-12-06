import { createRouter, createWebHistory } from 'vue-router'
import Login from '@/views/Login.vue'

import Perfil from '@/views/Perfil.vue'
import EditarPerfilCard from '@/components/perfil/EditarPerfilCard.vue'

import Publicaciones from '@/views/Publicaciones.vue';
import CrearPublicacion from '@/components/publicaciones/CrearPublicacion.vue';
import DetallesPublicacion from '@/components/publicaciones/DetallesPublicacion.vue';
import EditarPublicacion from '@/components/perfil/EditarPublicacion.vue';

import Salas from '@/views/Salas.vue';
import Chat from '@/views/Chat.vue';

import Noticias from '@/views/Noticias.vue'

import Biblioteca from '@/views/Biblioteca.vue'
import Peliculas from '@/components/biblioteca/Peliculas.vue'
import Series from '@/components/biblioteca/Series.vue'
import Libros from '@/components/biblioteca/Libros.vue'
import LibrosAutores from '@/components/biblioteca/LibrosAutores.vue';
import CrearLibroForm from '@/components/biblioteca/CrearLibroForm.vue';

import Premium from '@/views/Premium.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login,
    },
    {
      path: '/perfil/:id',
      name: 'Perfil',
      component: Perfil,
      children: [
          { path: 'editar', name: 'EditarPerfil', component: EditarPerfilCard },
          { path: 'publicacion-editar/:publicacionid', name: 'EditarPublicacion', component: EditarPublicacion }
      ],
    },
    {
      path: '/publicaciones',
      name: 'Publicaciones',
      component: Publicaciones,
      children: [
        { path: 'crear', name: 'CrearPublicacion', component: CrearPublicacion },
        { path: 'detalles/:id', name: 'DetallesPublicacion', component: DetallesPublicacion }
      ],
    },
    {
      path: '/biblioteca',
      name: 'Biblioteca',
      component: Biblioteca,
      children: [
          { path: 'peliculas', name: 'Peliculas', component: Peliculas },
          { path: 'series', name: 'Series', component: Series },
          { path: 'libros', name: 'Libros', component: Libros },
          { path: 'libros-autores', name: 'LibrosAutores', component: LibrosAutores },
          { path: 'crear-libro', name: 'CrearLibro', component: CrearLibroForm }
      ],
    },
    {
      path: '/noticias',
      name: 'Noticias',
      component: Noticias,
    },
    {
      path: '/salas',
      name: 'Salas',
      component: Salas,
    },
    {
      path: '/premium',
      name: 'Premium',
      component: Premium,
    },
    {
      path: '/chat',
      name: 'Chat',
      component: Chat,
      props: route => ({ registroAcceso: JSON.parse(route.query.registroAcceso) }) // Aceptar props
    }
  ]
})

export default router