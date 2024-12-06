<template>
    <div class="modal-editar-sala">
      <div class="sala-contenedor">
        <legend>Editar Sala</legend>
        <form @submit.prevent="editarSala">
          <div class="input-container">
            <label>Nombre:</label>
            <input v-model="sala.nombre" type="text" required />
          </div>
          <div class="input-container">
            <label class="label-privado">Privado:</label>
            <input v-model="sala.privado" type="checkbox" class="input-privado" />
          </div>
          <div class="input-container" v-if="sala.privado">
            <label>Contraseña:</label>
            <input v-model="sala.contrasena" type="password" />
          </div>
          <div class="input-container">
            <label>Descripción:</label>
            <textarea v-model="sala.descripcion_corta"></textarea>
          </div>
          <div class="img-container">
            <div class="img-default">
              <img :src="imagenUrl" alt="Imagen de la sala" />
            </div>
            <input id="file-upload" type="file" accept="image/*" @change="guardarImagen" />
            <div class="img-actions">
              <label for="file-upload" class="file-upload-btn">Subir Imagen</label>
              <button v-if="sala.multimedia || multimedia" type="button" class="borrar-img-btn" @click="borrarImagen">Borrar Imagen</button>
            </div>
          </div>
          <div class="acciones-btn">
            <button type="submit" class="btn-action btn-guardar">Guardar Cambios</button>
            <button type="button" class="btn-action btn-descartar" @click="cerrarModal">Cancelar</button>
          </div>
        </form>
      </div>
    </div>
  </template>
  
  <script setup>
  import { computed, ref, watch, onUnmounted } from 'vue';
  import axios from 'axios';
  import Swal from 'sweetalert2';
  const props = defineProps({
    sala: Object,
  });
  
  const emits = defineEmits(['close', 'salaEditada']);
  
  const token = ref(localStorage.getItem('token') || '');
  
  const sala = ref({ ...props.sala });
  const multimedia = ref(null);
  const objectUrl = ref(null);
  const eliminarMultimedia = ref(false); // Añadimos esta variable
  
  watch(() => props.sala, (newSala) => {
    sala.value = { ...newSala };
  }, { deep: true });
  
  const imagenUrl = computed(() => {
    if (multimedia.value) {
      if (objectUrl.value) URL.revokeObjectURL(objectUrl.value);
      objectUrl.value = URL.createObjectURL(multimedia.value);
      return objectUrl.value;
    }
    return sala.value.multimedia ? `http://localhost:8000/static/salas/${sala.value.multimedia}?${Date.now()}` : '';
  });
  
  function guardarImagen(event) {
    if (event.target.files.length > 0) {
      multimedia.value = event.target.files[0];
      eliminarMultimedia.value = false; // No eliminamos la imagen si hay una nueva seleccionada
    }
  }
  
  function borrarImagen() {
    multimedia.value = null;
    eliminarMultimedia.value = true; // Marcamos que queremos eliminar la imagen
    if (objectUrl.value) {
      URL.revokeObjectURL(objectUrl.value);
      objectUrl.value = null;
    }
    sala.value.multimedia = null; // También reseteamos el multimedia de la sala
  }
  
  async function editarSala() {
    const formData = new FormData();
    formData.append('id_perfil', sala.value.id_perfil);
    formData.append('nombre', sala.value.nombre);
    formData.append('privado', sala.value.privado);
    formData.append('contrasena', sala.value.contrasena);
    formData.append('descripcion_corta', sala.value.descripcion_corta);
    formData.append('eliminar_multimedia', eliminarMultimedia.value.toString()); // Asegurarse de añadir este campo
  
    if (multimedia.value) {
      formData.append('multimedia', multimedia.value);
    }
  
    try {
      await axios.put(`http://localhost:8000/salas/${sala.value.id_sala}`, formData, {
        headers: {
          Authorization: `Bearer ${token.value}`,
          "Content-Type": "multipart/form-data"
        }
      });
      Swal.fire({
      title: 'Sala editada!',
      icon: 'success',
      text: 'La sala se edito satisfactoriamente!'
      });
      emits('salaEditada');
      cerrarModal();
    } catch (error) {
      console.error('Error al editar la sala:', error);
    }
  }
  
  function cerrarModal() {
    emits('close');
  }
  
  onUnmounted(() => {
    if (objectUrl.value) URL.revokeObjectURL(objectUrl.value);
  });
  </script>
  
  
  <style scoped>
  .modal-editar-sala {
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
    z-index: 9999; /* Alto valor para asegurar que esté por encima de otros elementos */
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
  