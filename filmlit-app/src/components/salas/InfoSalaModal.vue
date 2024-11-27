<template>
  <div class="modal-info-sala">
    <div class="sala-contenedor">
      <button class="btn-cerrar" @click="cerrarModal">X</button>
      <img :src="imagenUrl" alt="Imagen de la sala" class="sala-imagen" />
      <div class="info-container">
        <div class="info-item">
          <p id="nombre">{{ sala.nombre }} <i id="candado" :class="sala.privado ? 'fas fa-lock' : 'fas fa-lock-open'"></i></p>
        </div>
        <div class="info-item">
          <p>{{ sala.descripcion_corta }}</p>
        </div>
        <div class="info-item">
          <p>{{ sala.privado ? 'Privado' : 'Publico' }}</p>
        </div>
        <div class="info-item" v-if="sala.privado">
          <label>Id: {{ sala.id_sala }}</label>
        </div>
        <div class="info-item">
          <label>Miembros:</label>
          <p>{{ sala.miembros }} miembros</p>
        </div>
      </div>
      <div class="acciones-btn">
        <button type="button" class="btn-action btn-unirme" @click="unirmeSala">Unirme</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import axios from 'axios';

const props = defineProps({
  sala: Object,
});

const emits = defineEmits(['close']);

const token = ref(localStorage.getItem('token') || '');

const imagenUrl = computed(() => {
  return props.sala.multimedia ? `http://localhost:8000/static/salas/${props.sala.multimedia}?${Date.now()}` : 'http://localhost:8000/static/salas/pfp-icon.jpg';
});

async function unirmeSala() {
  try {
    await axios.post(`http://localhost:8000/salas/${props.sala.id_sala}/unirme`, {}, {
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    });
    alert('Te has unido a la sala con éxito!');
    cerrarModal();
  } catch (error) {
    console.error('Error al unirse a la sala:', error);
    alert('Error al unirse a la sala.');
  }
}

function cerrarModal() {
  emits('close');
}
</script>

<style scoped>
#candado {
  font-size: 20px;
  color: var(--color-text-secundary);
}

#nombre {
  font-size: 35px;
  font-weight: 600;
  border-bottom: 1px solid var(--color-border);
}

.sala-contenedor {
  width: 100%;
  max-width: 450px;
  background-color: var(--background-color-primary);
  color: var(--color-text-primary);
  border: 2px solid var(--color-border);
  border-radius: 20px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  position: relative;
}

.sala-imagen {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.info-container {
  padding: 15px;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.acciones-btn {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 10px;
}

.btn-action {
  width: 80px;
  height: 35px;
  margin: 20px;
  background-color: var(--background-color-primary);
  color: var(--color-text-primary);
  font-weight: bold;
  font-size: 14px;
  border: 1px solid var(--color-border);
  border-radius: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.btn-action:hover {
  background-color: green;
}

.btn-cerrar {
  position: absolute;
  top: 10px;
  right: 10px;
  width: 30px;
  height: 30px;
  background-color: rgba(128, 128, 128, 0.568);
  color: var(--color-text-primary);
  border-radius: 50%;
  border: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 18px;
}

.modal-info-sala {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}
</style>
