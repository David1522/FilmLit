<template>
  <div class="modal-crear-sala">
    <div class="sala-contenedor">
      <legend>Crear Nueva Sala</legend>
      <form @submit.prevent="crearSala">
        <div class="img-container">
          <div class="img-default">
            <img v-if="imagenUrl" :src="imagenUrl" alt="sala-image">
          </div>
          <div class="img-actions">
            <label for="file-upload" class="file-upload-btn">Añadir Foto</label>
            <input id="file-upload" type="file" accept="image/*" :multiple="false" @change="guardarImagen"/>
            <button type="button" class="borrar-img-btn" @click="borrarImagen">Borrar</button>
          </div>
        </div>

        <div class="input-container">
          <label for="nombre" class="label-nombre">Nombre: </label>
          <input type="text" id="nombre" v-model="sala.nombre" class="input-nombre" required>
        </div>

        <div class="input-container">
          <label for="privado" class="label-privado">Privado: </label>
          <input type="checkbox" id="privado" v-model="sala.privado" class="input-privado" @change="togglePasswordInput">
        </div>

        <div class="input-container" v-if="sala.privado">
          <label for="contrasena" class="label-contrasena">Contraseña: </label>
          <input type="password" id="contrasena" v-model="sala.contrasena" class="input-contrasena" required>
        </div>

        <div class="input-container">
          <label for="descripcion_corta" class="label-descripcion-corta">Descripción: </label>
          <textarea id="descripcion_corta" class="input-descripcion-corta" rows="4" cols="50" maxlength="255" v-model="sala.descripcion_corta" required></textarea>
        </div>

        <div class="acciones-btn">
          <button type="submit" class="btn-action btn-guardar">Crear Sala</button>
          <button type="button" class="btn-action btn-descartar" @click="cerrarModal">Cancelar</button>
        </div>
      </form>
      <p v-if="mensaje">{{ mensaje }}</p>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';

const emits = defineEmits(['close', 'salaCreada']);

const token = ref(localStorage.getItem('token') || '');

const sala = ref({
  nombre: '',
  privado: false,
  contrasena: '',
  descripcion_corta: ''
});
const multimedia = ref(null);
const objectUrl = ref(null);
const mensaje = ref('');

const imagenUrl = computed(() => {
  if (multimedia.value) {
    return objectUrl.value = URL.createObjectURL(multimedia.value);
  } else {
    return null;
  }
});

function guardarImagen(event) {
  if (event.target.files.length > 0) {
    multimedia.value = event.target.files[0];
  }
}

function borrarImagen() {
  multimedia.value = null;
  if (objectUrl.value) {
    URL.revokeObjectURL(objectUrl.value);
    objectUrl.value = null;
  }
}

async function crearSala() {
  if (!token.value) {
    alert('Por favor inicie sesión para crear una sala.');
    return;
  }

  const formData = new FormData();
  formData.append('nombre', sala.value.nombre);
  formData.append('privado', sala.value.privado);
  if (sala.value.privado && sala.value.contrasena) {
    formData.append('contrasena', sala.value.contrasena);
  }
  formData.append('descripcion_corta', sala.value.descripcion_corta);
  if (multimedia.value) {
    formData.append('multimedia', multimedia.value);
  }

  try {
    await axios.post('http://localhost:8000/salas', formData, {
      headers: {
        Authorization: `Bearer ${token.value}`,
        "Content-Type": "multipart/form-data"
      }
    });

    Swal.fire({
        title: '¡Sala Creada!',
        icon: 'success',
        text: 'La sala ha sido creada satisfactoriamente.'
    })

    emits('salaCreada');
    cerrarModal();
  } catch (error) {
    if (error.response && error.response.status === 403) {
      mensaje.value = "No tienes permisos para crear una sala.";
    } else {
      console.error(error);
      mensaje.value = 'Error al crear la sala.';
    }
  }
}

function cerrarModal() {
  emits('close');
}
</script>

<style scoped>
.modal-crear-sala {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: var(--background-color-blur);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
}

.sala-contenedor {
  width: 100%;
  max-width: 450px;
  height: auto;
  background-color: var(--background-color-primary);
  color: var(--color-text-primary);
  border: 2px solid var(--color-border);
  border-radius: 8px;
  padding: 20px;
}

legend {
  text-align: center;
  font-size: 20px;
  margin-bottom: 15px;
}

label {
  font-size: 14px;
}

.img-container {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.img-default > img {
  width: 150px;
  height: 150px;
  border-radius: 15px;
  margin-bottom: 5px;
}

input[type="file"] {
  display: none;
}

.img-actions {
  width: 100%;
  padding: 10px;
  display: flex;
  justify-content: center;
}

.file-upload-btn, .borrar-img-btn {
  font-size: 14px;
  font-weight: 100;
  border-radius: 10px;
  padding: 5px 10px;
  cursor: pointer;
}

.file-upload-btn {
  border: 1px solid var(--color-border);
  margin-right: 9px;
}

.borrar-img-btn {
  background-color: transparent;
  color: var(--color-text-primary);
  border: 1px solid var(--color-border);
}

.input-container {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.label-privado {
  flex-grow: 1;
}

.input-privado {
  margin-left: auto;
}

.input-container > input, textarea {
  background-color: var(--background-color-primary);
  color: var(--color-text-primary);
  min-height: 30px;
  padding: 8px 15px;
  border: 1px solid var(--color-border);
  border-radius: 5px;
  resize: none;
  flex-grow: 1;
}

.acciones-btn {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.btn-action {
  justify-self: end;
  width: 75px;
  height: 30px;
  background-color: var(--background-color-primary);
  color: var(--color-text-primary);
  font-weight: 600;
  font-size: 14px;
  border: 1px solid var(--color-border);
  border-radius: 15px;
  margin: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  cursor: pointer;
  transition: background-color 200ms ease-in-out;
}

.btn-action:hover {
  background-color: green;
  transition: background-color 200ms ease-in-out;
}

.cargando {
  text-align: center;
  padding: 20px;
}

@media (max-width: 800px) {
  .sala-contenedor {
    width: 80%;
  }
}
</style>

