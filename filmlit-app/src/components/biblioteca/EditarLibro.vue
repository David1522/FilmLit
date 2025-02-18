<template>
    <div class="model-editar-libro">
        <div class="editar-libro-container">
            <legend>Editar Libro</legend>
            <form @submit.prevent="updateLibro">
                <!-- Imagen Obligatoria -->
                <div class="img-container">
                    <div class="img-default">
                        <img v-if="imagenUrl" :src="imagenUrl" alt="imagen-libro">
                    </div>
                    <div class="img-actions">
                        <label for="file-upload" class="file-upload-btn">Cambiar Portada</label>
                        <input
                            id="file-upload"
                            type="file"
                            accept="image/*"
                            @change="guardarImagen"
                        />
                    </div>
                </div>

                <!-- Título -->
                <div class="input-container">
                    <label for="titulo" class="label-titulo">Título:</label>
                    <input
                        id="titulo"
                        name="titulo"
                        type="text"
                        class="input-titulo"
                        placeholder="Título del libro"
                        maxlength="255"
                        v-model="titulo"
                        required
                    />
                </div>

                <!-- Fecha de Publicación -->
                <div class="input-container">
                    <label for="fecha-publicacion" class="label-fecha">Fecha de Publicación:</label>
                    <input
                        id="fecha-publicacion"
                        name="fecha-publicacion"
                        type="date"
                        class="input-fecha"
                        v-model="fechaPublicacion"
                        required
                    />
                </div>

                <div class="acciones-btn">
                    <button type="submit" class="btn-action btn-guardar">Guardar</button>
                    <button
                        type="button"
                        class="btn-action btn-descartar"
                        @click="router.push('/biblioteca/libros-autores')"
                    >
                        Cancelar
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>

<script setup>
    import { ref, computed, onMounted, defineProps } from 'vue';
    import axios from 'axios';
    import Swal from 'sweetalert2';
    import router from '@/router';

    const token = ref('');

    const idLibro = ref('');
    const portada = ref(null);
    const titulo = ref('');
    const fechaPublicacion = ref('');

    const multimedia = ref(null);
    const objectUrl = ref(null);

    const props = defineProps({
        idBook: String
    });

    const imagenUrl = computed(() => {
        if (multimedia.value) {
            if (objectUrl.value) URL.revokeObjectURL(objectUrl);
            objectUrl.value = URL.createObjectURL(multimedia.value);
            return objectUrl.value;
        }

        if (portada.value) {
            return `http://localhost:8000/static/libros/${portada.value}?${Date.now()}`;
        }
    });

    async function fetchBookData() {
        validarToken();

        try {
            const response = await axios.get(`http://localhost:8000/libros/${props.idBook}`, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            });

            console.log(response.data);

            idLibro.value = response.data.id_libro;
            portada.value = response.data.portada;
            titulo.value = response.data.titulo;
            fechaPublicacion.value = response.data.fecha_publicacion.split("T")[0];
            
        } catch(error) {
            console.log(error);
        }
    }

    function guardarImagen(event) {
        if (event.target.files.length > 0) {
            multimedia.value = event.target.files[0];
        }
    }

    async function updateLibro () {
        validarToken();

        const formData = new FormData();
        formData.append('id_libro', idLibro.value);
        formData.append('titulo', titulo.value);
        formData.append('fecha_publicacion', fechaPublicacion.value);
        if (multimedia.value) formData.append('multimedia', multimedia.value);

        formData.forEach((arg) => {
            console.log(arg)
        })
        

        try {
            const response = await axios.put('http://localhost:8000/libro', formData, {
                headers: {
                    Authorization: `Bearer ${token.value}`,
                    'Content-Type': 'multipart/form-data'
                }
            });

            Swal.fire({
                icon: 'success',
                title: 'Publicacion Actualizada',
                text: response.data.mensaje
            });

            router.push('/biblioteca/libros-autores')
        } catch (error) {
            Swal.fire({
                icon: 'error',
                title: 'Error al Actualizar Publicacion',
                text: error.response?.data?.detail || 'Error desconocido',
            });

            console.log(error);
            router.go(-1);
        }
    }

    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('/');
            return;
        }
    }

    onMounted(() => {
        fetchBookData();
    });
</script>

<style lang="scss">
    .model-editar-libro {
        width: 100%;
        height: 100%;
        background-color: var(--background-color-blur);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        display: flex;
        align-items: center;
        justify-content: center;
        position: absolute;
        top: 0;
        left: 0;
    }

    .editar-libro-container {
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
        width: 200px;
        height: 250px;
        border-radius: 15px;
        margin-bottom: 5px;
        object-fit: cover;
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
</style>