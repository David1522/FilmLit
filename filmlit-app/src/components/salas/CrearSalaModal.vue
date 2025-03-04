<template>
    <div class="modal-crear-sala">
        <div class="sala-contenedor">
            <h2 class="title">Crear Nueva Sala</h2>

            <form @submit.prevent="crearSala" class="form">
                <div class="img-container">
                    <div class="img-preview" v-if="imagenUrl">
                        <img :src="imagenUrl" alt="sala-image">
                    </div>
                    <div class="img-actions">
                        <label for="file-upload" class="btn-upload">Añadir Foto</label>
                        <input id="file-upload" type="file" accept="image/*" :multiple="false" @change="guardarImagen" hidden/>
                        <button type="button" class="btn-delete" @click="borrarImagen">Borrar</button>
                    </div>
                </div>

                <div class="input-group">
                    <label for="nombre">Nombre</label>
                    <input type="text" id="nombre" v-model="sala.nombre" required>
                </div>

                <div class="input-group checkbox-group">
                    <input type="checkbox" id="privado" v-model="sala.privado" @change="togglePasswordInput">
                    <label for="privado">Privado</label>
                </div>

                <div class="input-group" v-if="sala.privado">
                    <label for="contrasena">Contraseña</label>
                    <input type="password" id="contrasena" v-model="sala.contrasena" required>
                </div>

                <div class="input-group">
                    <label for="descripcion_corta">Descripción</label>
                    <textarea id="descripcion_corta" rows="4" maxlength="255" v-model="sala.descripcion_corta" required></textarea>
                </div>

                <div class="btn-group">
                    <button type="submit" class="btn-primary">Crear Sala</button>
                    <button type="button" class="btn-secondary" @click="cerrarModal">Cancelar</button>
                </div>
            </form>
            
            <p v-if="mensaje" class="message">{{ mensaje }}</p>
        </div>
    </div>
</template>

<script setup>
    import { computed, ref } from 'vue';
    import axios from 'axios';
    import Swal from 'sweetalert2';

    const emits = defineEmits(['close', 'salaCreada']);

    const token = ref('');

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

        validarToken();

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
                Swal.fire({
                    title: 'Error al crear la sala.',
                    icon: 'error',
                    text: error.response.data.detail
                })
                console.log(error)
            }
        }
    }

    function cerrarModal() {
        emits('close');
    }

    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('login');
            return;
        }
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
        background: var(--background-color-primary);
        color: var(--color-text-primary);
        border: 2px solid var(--color-border);
        padding: 2rem;
        border-radius: 8px;
        width: 400px;
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
    }

    .title {
        font-size: 1.5rem;
        text-align: center;
        margin-bottom: 1rem;
    }

    .form {
        display: flex;
        flex-direction: column;
        gap: 1rem;
    }

    .img-container {
        text-align: center;
        margin-bottom: 1rem;
    }

    .img-preview img {
        width: 100px;
        height: 100px;
        object-fit: cover;
        border-radius: 8px;
    }

    .img-actions {
        display: flex;
        justify-content: center;
        gap: 0.5rem;
        margin-top: 0.5rem;
    }

    .btn-upload,
    .btn-delete {
        background: #ddd;
        color: #000;
        border: none;
        padding: 0.5rem 1rem;
        border-radius: 5px;
        cursor: pointer;
        font-size: 0.9rem;
    }

    .btn-delete {
        background: #ff4d4d;
        color: white;
    }

    .input-group {
        display: flex;
        flex-direction: column;
        gap: 0.3rem;
    }

    input, textarea {
        padding: 0.6rem;
        border: 1px solid #ccc;
        border-radius: 5px;
        font-size: 1rem;
        width: 100%;
    }

    .checkbox-group {
        display: flex;
        align-items: center;
        gap: 0.5rem;
    }

    .btn-group {
        display: flex;
        justify-content: space-between;
    }

    .btn-primary {
        background: #4CAF50;
        color: white;
        border: none;
        padding: 0.7rem;
        border-radius: 5px;
        cursor: pointer;
        width: 48%;
        font-size: 1rem;
    }

    .btn-secondary {
        background: #ccc;
        border: none;
        padding: 0.7rem;
        border-radius: 5px;
        cursor: pointer;
        width: 48%;
        font-size: 1rem;
    }

    .message {
        text-align: center;
        color: #d9534f;
        margin-top: 1rem;
    }
</style>