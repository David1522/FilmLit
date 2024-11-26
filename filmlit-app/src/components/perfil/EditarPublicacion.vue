<template>
    <div class="modal-editar-publicacion">
        <div class="editar-publicacion-container" v-if="publicacion">
            <legend>Editar Perfil</legend>
            <form @submit.prevent="updatePublicacion">
                <div class="multimedia-container">
                    <div class="multimedia-default" v-if="multimediaUrl">
                        <img :src="multimediaUrl" :key="multimediaUrl"alt="default-pfp" />
                    </div>

                    <div>
                        <label for="file-upload" class="file-upload-btn">Subir Foto</label>
                        <input id="file-upload" type="file" accept="image/*" :multiple="false" @change="guardarImagen"/>
                        <button v-if="publicacion.multimedia || multimedia" @click="borrarFotoPerfil" type="button" class="borrar-multimedia-btn">Borrar</button>
                    </div>
                </div>

                <div class="input-container">
                    <label for="descripcion">Descripcion:</label>
                    <textarea name="descripcion" id="descripcion" rows="4" cols="50" placeholder="Escribe algo acerca de ti..." maxlength="280" v-model="descripcion"></textarea>
                </div>

                <div class="acciones-btn">
                    <button type="submit" class="btn-action btn-guardar">Guardar</button>
                    <button type="button" class="btn-action btn-descartar" @click="router.push(`/perfil/${perfilId}`);">Descartar</button>
                </div>
            </form>
        </div>
        <div class="cargando" v-else>
            <p>Cargando Publicacion...</p>
        </div>
    </div>
</template>

<script setup>
    import { ref, onMounted, computed, onUnmounted } from 'vue';
    import { useRoute } from 'vue-router';
    import Swal from 'sweetalert2';
    import router from '@/router';
    import axios from 'axios';

    const route = useRoute();
    const emits = defineEmits();

    const perfilId = ref(route.params.id);
    const idPublicacion = ref(route.params.publicacionid);
    const token = ref('');

    const publicacion = ref(null)
    const descripcion = ref('')
    const multimedia = ref(null); // Guarda el archivo seleccionado
    const objectUrl = ref(null) // Guarda la URL para el archivo seleccionado por el usuario
    const deleteMultimedia = ref(false); // Guarda si el usuario decide borrar su la imagen

    async function fetchPublicacion() {
        validarToken();

        try {
            const response = await axios.get(`http://localhost:8000/publicaciones/${idPublicacion.value}`, {
                headers: {
                    Authorization: `Bearer ${token.value}`,
                }
            });
            publicacion.value = response.data;
            descripcion.value = publicacion.value.descripcion;
        } catch (error) {
            console.log(error);
            localStorage.removeItem('token');
            router.push('/');
        }
    }

    // Computed property para mostrar la imagen de la publicacion dinamicamente
    const multimediaUrl = computed(() => {
        // Muestra la imagen si hay un archivo subido al form
        if (multimedia.value) {
            if (objectUrl.value) URL.revokeObjectURL(objectUrl); // Limpia la URL previa
            objectUrl.value = URL.createObjectURL(multimedia.value);
            return objectUrl.value;
        }
        // Si deleteMultimedia es true, se mostrará la imagen por defecto
        if (deleteMultimedia.value) {
            return ``;
        }
        // Muestra la foto de perfil del usuario si existe
        if (publicacion.value && publicacion.value.multimedia) {
            return `http://localhost:8000/static/publicaciones/${publicacion.value.multimedia}?${Date.now()}`
        }
    });

    function guardarImagen(event) {
        if (event.target.files.length > 0) {
            multimedia.value = event.target.files[0];
            deleteMultimedia.value = false;
        }
    }

    function borrarFotoPerfil() {
        multimedia.value = null;
        deleteMultimedia.value = true;
        if (objectUrl.value) {
            URL.revokeObjectURL(objectUrl);
            objectUrl.value = null;
        }
    }

    async function updatePublicacion() {
        validarToken();

        const formData = new FormData();
        formData.append('id_publicacion', idPublicacion.value)
        if (descripcion.value) formData.append('descripcion', descripcion.value);
        if (multimedia.value) formData.append('multimedia', multimedia.value);
        if (deleteMultimedia.value) formData.append('eliminar_multimedia', deleteMultimedia.value);

        try {
            const response = await axios.put('http://localhost:8000/publicaciones', formData, {
                headers: {
                    Authorization: `Bearer ${token.value}`,
                    'Content-Type': 'multipart/form-data',
                }
            });

            Swal.fire({
                icon: 'success',
                title: 'Publicacion Actualizada',
                text: response.data.mensaje
            });

            emits('posts-updated');
            router.push(`/perfil/${perfilId.value}`);
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
        fetchPublicacion();
    })
</script>

<style scoped>
    .modal-editar-publicacion {
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

    .editar-publicacion-container {
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

    .multimedia-container {
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        margin-bottom: 10px
    }

    .multimedia-default > img {
        width: 250px;
        height: 250px;
        border-radius: 10px;
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
        margin-right: 9px;
        cursor: pointer;
    }

    .file-upload-btn:hover {
        background-color: var(--background-color-secondary);
    }

    .borrar-multimedia-btn {
        background-color: transparent;
        color: #fff;
        border: 1px solid var(--color-border);
        font-size: 14px;
        font-weight: 100;
        border-radius: 10px;
        padding: 5px 10px;
        cursor: pointer;
        transition: background-color ease-out 300ms;
    }

    .borrar-multimedia-btn:hover {
        background-color: red;
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
</style>