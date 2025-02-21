<template>
    <div class="modal-details-book">
        <div class="book-container">
            <div class="img-container">
                <img v-if="imagenUrl" :src="imagenUrl" alt="imagen-libro">
            </div>

            <div class="data-container">
                <p class="bd-title">{{ titulo }}</p>
                <p class="bd-attribute">Autor: <span class="book-data">{{ usuario?.nombre_usuario || 'Cargando Usuario' }}</span></p>
                <p class="bd-attribute">Fecha Publicacion: <span class="book-data"> {{ fechaPublicacion }} </span></p>
                <p class="bd-attribute">Guardados: <span class="book-data">25</span></p>

                <div class="favorite-container">
                    <i class="fa-regular fa-heart" title="Añadir a favoritos"></i>
                </div>
            </div>

            <div class="go-back-btn" @click="router.go(-1)">
                <fa icon="arrow-left"/>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { ref, onMounted, computed } from 'vue';
    import axios from 'axios';
    import router from '@/router';

    const idLibro = ref('');
    const portada = ref(null);
    const titulo = ref('');
    const fechaPublicacion = ref('');
    const usuario = ref(null);

    const props = defineProps({
        idBook: String
    });

    const imagenUrl = computed(() => {
        if (portada.value) {
            return `http://localhost:8000/static/libros/${portada.value}?${Date.now()}`;
        }
    });

    const token = ref('');

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
            usuario.value = response.data.usuario;
            
        } catch(error) {
            console.log(error);
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

<style scoped>
    .modal-details-book {
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

    .book-container {
        width: 100%;
        max-width: 700px;
        height: 100%;
        max-height: 700px;
        background-color: var(--background-color-primary);
        color: var(--color-text-primary);
        border: 2px solid var(--color-border);
        border-radius: 8px;
        padding: 20px;
        display: flex;
        position: relative;
    }

    .img-container {
        width: 350px;
        border-radius: 15px;
    }

    .img-container > img {
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-radius: 15px;
    }

    .data-container {
        flex: 1;
        padding: 5px 25px;
        display: flex;
        flex-direction: column;
        gap: 3px;
    }

    .bd-title {
        font-size: 20px;
        font-weight: 600;
        margin-bottom: 15px;
    }

    .bd-attribute {
        font-size: 14px;
        font-weight: 600;
    }

    .data-container > p > span {
        font-size: 14px;
        font-weight: 100;
    }

    .favorite-container {
        width: 100%;
        display: flex;
        justify-content: left;
    }

    .fa-regular {
        font-size: 20px;
        font-weight: 500;
        padding: 5px;
        cursor: pointer;
    }

    .fa-regular:hover {
        font-weight: 600;
    }

    .go-back-btn {
        width: 30px;
        height: 30px;
        font-size: 20px;
        border-radius: 100%;
        border: 2px solid var(--color-text-primary);
        margin: 5px;
        position: absolute;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        transition: transform 200ms ease-in-out;
    }

    .go-back-btn:hover {
        transform: scale(1.2);
        transition: transform 300ms ease-in-out;
    }

    @media (max-width: 1200px) { 
        .book-container {
            width: 70%;
            flex-direction: column;
        }

        .img-container {
            width: 100%;
            height: 460px;
            margin-bottom: 10px;
        }

        .data-container {
            height: auto;
        }
    }
</style>