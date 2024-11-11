<template>
    <div class="modal-crear-publicacion">
        
        <div class="publicacion-contenedor">
            <legend>Nueva Publicación</legend>
            <form @submit.prevent="crearPublicacion">
                <div class="img-container">
                    <div class="img-container">
                        <div class="img-default">
                            <img v-if="imagenUrl" :src="imagenUrl" alt="publ-image">
                        </div>

                        <div class="img-actions">
                            <label for="file-upload" class="file-upload-btn">Añadir Foto</label>
                            <input id="file-upload" type="file" accept="image/*" :multiple="false" @change="guardarImagen"/>
                            <button type="button" class="borrar-img-btn" @click="borrarImagen">Borrar</button>
                        </div>
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
    import { computed, onBeforeMount, ref } from 'vue';
    import axios from 'axios';
    import Swal from 'sweetalert2';
    import router from '@/router';

    const emits = defineEmits();
    
    const token = ref('');

    const multimedia = ref(null);
    const descripcion = ref('');
    const objectUrl = ref(null)

    async function validarToken() {
        token.value = localStorage.getItem('token');
        if(!token) {
            router.push('/');
            return;
        }
        console.log("Token validado: ", token.value)
    }


    async function crearPublicacion() {
        validarToken();

        const formData = new FormData();
        if (multimedia.value) formData.append('multimedia', multimedia.value);
        if (descripcion.value) formData.append('descripcion', descripcion.value);

        if (multimedia.value || descripcion.value) {
            try {
                const response = await axios.post('http://localhost:8000/publicaciones', formData, {
                    headers: {
                        Authorization: `Bearer ${token.value}`,
                        "Content-Type": "multipart/form-data"
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
                Swal.fire({
                    icon: 'error',
                    title: 'Error en al Crear Publicacion',
                    text: error.response.data.detail
                });

                console.log(error);
                router.push('/publicaciones');
            }
        } else {
            Swal.fire({
                icon: 'error',
                title: 'Error en los Campos',
                text: "Asegurate de llenar la información en los campos antes de crear una publicacion."
            });
        }
    }

    const imagenUrl = computed(() => {
        if (multimedia.value) {
            return objectUrl.value = URL.createObjectURL(multimedia.value);
        } else {
            return null;
        }
    })

    function guardarImagen(event) {
        if (event.target.files.length > 0) {
            multimedia.value = event.target.files[0];
        }
    }

    function borrarImagen() {
        multimedia.value = null;
        if (objectUrl.value) {
            URL.revokeObjectURL(objectUrl);
            objectUrl.value = null;
        }
    }

    onBeforeMount(() => {
        if (objectUrl.value) {
            URL.revokeObjectURL(objectUrl.value);
        }
    })
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
    }

    .file-upload-btn {
        border: 1px solid var(--color-border);
        font-size: 14px;
        font-weight: 100;
        border-radius: 10px;
        padding: 5px 10px;
        margin-right: 9px;
        cursor: pointer;
    }

    .borrar-img-btn {
        background-color: transparent;
        color: var(--color-text-primary);
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

