<template>
  <div class="salas-container">
    <ul v-if="salas.length" class="salas-list">
      <SalaItem
        v-for="sala in salas"
        :key="sala.id_sala"
        :sala="sala"
        :user-id="userId"
        @salaEditada="fetchSalas"
        @salaEliminada="fetchSalas"
      />
    </ul>
    <p v-else class="no-salas">No hay salas disponibles.</p>
  </div>
</template>

<script>
import SalaItem from './SalaItem.vue';
import axios from 'axios';

export default {
  components: {
    SalaItem
  },
  props: {
    userId: Number // Recibimos el ID del usuario actual
  },
  data() {
    return {
      salas: []
    };
  },
  created() {
    this.fetchSalas();
  },
  methods: {
    async fetchSalas() {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:8000/salas', {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        this.salas = response.data;
      } catch (error) {
        console.error(error);
        alert('Error al cargar las salas.');
      }
    }
  }
};
</script>


<style scoped>
.salas-container {
  color: var(--color-text-primary);
  display: flex;
  flex-direction: column;
  align-items: center;
  overflow-y: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.salas-container::-webkit-scrollbar {
  display: none;
}

.salas-list {
  width: 100%;
  list-style-type: none;
  padding: 0;
}

.no-salas {
  color: var(--color-text-secundary);
  font-size: 16px;
  padding-top: 15px;
  text-align: center;
}
</style>
