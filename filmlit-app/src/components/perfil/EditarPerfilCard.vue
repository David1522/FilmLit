<template>
    <div class="modal-editar-perfil">
        <div class="editar-perfil-container" v-if="perfil">
            <legend>Editar Perfil</legend>
            <form @submit.prevent="updatePerfilUsuario">
                <div class="pfp-container">
                    <div class="pfp-default">
                        <img :src="perfil.foto_perfil ? `http://localhost:8000/static/fotos_perfil/${perfil.id_perfil}.jpg`
            : 'http://localhost:8000/static/fotos_perfil/pfp-icon.jpg'" alt="default-pfp"/>
                    </div>
                    <label for="file-upload" class="file-upload-btn">Cambiar Foto de Perfil</label>
                    <input id="file-upload" type="file" accept="image/*" @change="guardarImagen"/>
                </div>

                <div class="input-container">
                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" maxlength="255" v-model="nombre">
                </div>

                <div class="input-container">
                    <label for="fecha_nacimiento">Fecha de Nacimiento:</label>
                    <input type="date" id="fecha_nacimiento" name="fecha_nacimiento" min="1900-01-01" v-model="fechaNacimiento">
                </div>

                <div class="input-container">
                    <label for="descripcion">Descripcion:</label>
                    <textarea name="descripcion" id="descripcion" rows="4" cols="50" placeholder="Escribe algo acerca de ti..." maxlength="280" v-model="descripcion"></textarea>
                </div>

                <div class="acciones-btn">
                    <button type="submit" class="btn-action btn-guardar">Guardar</button>
                    <button type="button" class="btn-action btn-descartar" @click="router.push('/perfil')">Descartar</button>
                </div>
            </form>
        </div>
        <div class="cargando" v-else>
            <p>Cargando Perfil...</p>
        </div>
    </div> 
</template>

<script setup>
    import { ref, onMounted } from 'vue';
    import axios from 'axios';
    import router from '@/router';
    import Swal from 'sweetalert2';
    import { defineEmits } from 'vue';

    const emits = defineEmits();
    const token = ref('');

    const perfil = ref(null);
    const nombre = ref('');
    const fechaNacimiento = ref('');
    const descripcion = ref('');
    const fotoPerfil = ref(null)


    async function validarToken() {
        token.value = localStorage.getItem('token')
        if (!token) {
            router.push('/login');
            return;
        }
    }


    async function fetchPerfilUsuario() {
        validarToken();

        try {
            const response = await axios.get('http://localhost:8000/perfil/me', {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            });

            perfil.value = response.data;
            nombre.value = perfil.value.nombre;
            fechaNacimiento.value = perfil.value.fecha_nacimiento;
            descripcion.value = perfil.value.descripcion;
        } catch (error) {
            console.log(error);
            localStorage.removeItem('token');
            router.push('/login');
        }
    }


    function guardarImagen(event) {
        fotoPerfil.value = event.target.files[0]
    }


    async function updatePerfilUsuario() {
        validarToken();
        
        const formData = new FormData();
        formData.append('nombre', nombre.value);
        formData.append('fecha_nacimiento', fechaNacimiento.value);
        formData.append('descripcion', descripcion.value);
        formData.append('foto_perfil', fotoPerfil.value);

        try {
            const response = await axios.put('http://localhost:8000/perfil/me', formData, {
                headers: {
                    Authorization: `Bearer ${token.value}`,
                    'Content-Type': 'multipart/form-data',
                }
            });

            Swal.fire({
                icon: 'success',
                title: 'Perfil Actualizado',
                text: response.data.mensaje
            });

            emits('perfil-updated'); // Emite un evento para notidicartle al componente padre acerca del update
            router.push('/perfil');
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: 'Error al Actualizar Perfil',
                text: error.response.data.detail,
            });

            console.log(error);
            router.push('/perfil');
        }
    }
    
    
    onMounted(() => {
        fetchPerfilUsuario();
    })
</script>

<style scoped>
    .modal-editar-perfil {
        background-color: var(--background-color-blur);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        width: 100%;
        height: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        position: absolute;
        top: 0;
        left: 0;
    }

    .editar-perfil-container {
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
        margin-bottom: 15px
    }

    label {
        font-size: 14px;
    }

    .pfp-container {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        margin-bottom: 10px
    }

    .pfp-default > img {
        width: 150px;
        height: 150px;
        border-radius: 50%;
        margin-bottom: 5px;
    }

    input[type="file"] {
        display: none;
    }

    .file-upload-btn {
        border: 1px solid var(--color-border);
        font-size: 14px;
        font-weight: 100;
        border-radius: 10px;
        padding: 5px 10px;
        cursor: pointer;
    }

    .input-container {
        padding: 10px;
        display: flex;
        flex-direction: column;
        gap: 5px;
    }

    .input-container > input, textarea {
        background-color: var(--background-color-primary);
        color: var(--color-text-primary);
        min-height: 30px;
        padding: 8px 15px;
        border: 1px solid var(--color-border);
        border-radius: 5px;
    }

    .acciones-btn {
        display: flex;
        justify-content: center;
        gap: 15px;
    }

    .btn-action {
        color: #fff;
        width: 80px;
        height: 35px;
        font-weight: 600;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: transform ease-out 300ms;
    }

    .btn-action:hover {
        opacity: 0.8;
    }

    .btn-guardar {
        background-color: rgb(0, 94, 0);
    }

    .btn-descartar {
        background-color: red;
    }

    .cargando {
        text-align: center;
        padding: 20px;
    }

    @media (max-width: 800px) {
        .editar-perfil-container {
            width: 80%;
        }
    }
</style>