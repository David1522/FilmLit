<template>
    <div class="salas-main-container">
      <CrearSalaBtn :esVisible="puedeCrearSala" @abrirModal="mostrarCrearSala" />
      <SalaList :userId="userId" ref="salaListRef" />
      <RouterView @sala-creada="actualizarSalas" />
      <CrearSalaModal v-if="mostrarModalSala" @close="mostrarModalSala = false" @salaCreada="actualizarSalas" />
    </div>
  </template>
  
  <script setup>
  import CrearSalaBtn from '@/components/salas/CrearSalaBtn.vue';
  import SalaList from '@/components/salas/SalaList.vue';
  import CrearSalaModal from '@/components/salas/CrearSalaModal.vue';
  import { RouterView } from 'vue-router';
  import { ref, computed, onMounted } from 'vue';
  import axios from 'axios';
  
  const salaListRef = ref(null);
  const mostrarModalSala = ref(false);
  
  const tipoUsuario = ref('');
  const userId = ref(null); // Guardamos el ID del usuario actual
  
  const puedeCrearSala = computed(() => {
    return tipoUsuario.value === 'PREMIUM' || tipoUsuario.value === 'AUTOR';
  });
  
  function mostrarCrearSala() {
    mostrarModalSala.value = true;
  }
  
  async function actualizarSalas() {
    if (salaListRef.value) {
      await salaListRef.value.fetchSalas();
    }
  }
  
  async function obtenerDetallesUsuario() {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        throw new Error('No hay token disponible');
      }
      const response = await axios.get('http://localhost:8000/usuarios/me', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      tipoUsuario.value = response.data.tipo_usuario;
      userId.value = response.data.id_usuario; // Guardamos el ID del usuario actual
    } catch (error) {
      console.error('Error al obtener detalles del usuario:', error);
    }
  }
  
  onMounted(() => {
    obtenerDetallesUsuario();
  });
  </script>
  
  <style scoped>
  .salas-main-container {
    width: 100%;
    max-width: 750px;
    display: grid;
    grid-template: 60px 80vh / 1fr;
    position: relative;
    margin-left: auto;
    margin-right: auto;
  }
  
  @media (max-width: 800px) {
    .salas-main-container {
      grid-template: 60px 69vh / 1fr;
    }
  }
  </style>
  