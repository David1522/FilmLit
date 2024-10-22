<template>
    <div class="modal-crear-publicacion">
        
        <div class="publicacion-contenedor">
            <legend>Nueva Publicación</legend>
            <form @submit.prevent="crearPublicacion">
                <div class="img-container">
                    <div class="img-container">
                        <div class="img-default">
                            <img src="../icons/pfp-icon.jpg" alt="publ-image">
                        </div>
                        <label for="file-upload" class="file-upload-btn">Añadir Foto</label>
                        <input id="file-upload" type="file"/>
                    </div>
                </div>

                <div class="input-container">
                    <label for="descripcion-publicacion" class="label-descripcion-publicacion">Descripción:</label>
                    <textarea id="descripcion-publicacion" name="descripcion-publicacion" class="input-descripcion-publicacion" rows="4" cols="50" placeholder="¿En que estas pensando?..." maxlength="255" v-model="descripcion"></textarea>
                </div>

                <div class="acciones-btn">
                    <button type="submit" class="btn-action btn-guardar">Publicar</button>
                    <button type="button" class="btn-action btn-descartar" @click="router.push('/publicaciones')">Descartar</button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
    import { ref } from 'vue';
    import axios from 'axios';
    import Swal from 'sweetalert2';
    import router from '@/router';

    const emits = defineEmits();
    
    const token = ref('');

    const multimedia = ref(''); // Añadir funcionalidad despues
    const descripcion = ref('');

    async function validarToken() {
        token.value = localStorage.getItem('token');
        if(!token) {
            router.push('/login');
            return;
        }
        console.log("Token validado: ", token.value)
    }


    async function crearPublicacion() {
        validarToken();

        const publicacion = {
            descripcion: descripcion.value,
            multimedia: multimedia.value
        }

        if (publicacion.descripcion || publicacion.multimedia) {
            try {
                const response = await axios.post('http://localhost:8000/publicaciones', publicacion, {
                    headers: {
                    Authorization: `Bearer ${token.value}`
                    }
                });

                Swal.fire({
                    icon: 'success',
                    title: 'Publicacion Creada',
                    text: response.data.mensaje
                });

                emits('publicacion-creada');
                router.push('/publicaciones');
            } catch (error) {
                console.log(error);
                localStorage.removeItem('token');
                router.push('/login');
            }
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error en los Campos',
                text: 'Asegurate de haber llenado por lo menos un campo para crear una publicacion'
            });
        }
    }
</script>

<style scoped>
    .modal-crear-publicacion {
        position: absolute;
        width: 100%;
        height: 100%;
        background-color: var(--background-color-blur);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .publicacion-contenedor {
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

    .img-container {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        margin-bottom: 10px
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
        resize: none;
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

