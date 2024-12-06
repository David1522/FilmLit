<template>
    <div class="header-section">
        <h2 style="color: var(--color-text-primary); margin-bottom: 15px;">Libros de Nuestros Autores</h2>
        <button v-if="tipoUsuario === 'AUTOR'" @click="router.push('/biblioteca/crear-libro')" class="btn-crear">Añadir Obra</button>
    </div>

    <div class="scroll-container">
        <div v-for="libro in libros" :key="libro.id_libro" class="book-card">
            <div class="image-container">
            <img 
                :src="`http://localhost:8000/static/libros/${libro.portada}?${Date.now}`" 
                alt="Portada del libro" 
                class="book-image" 
            />
            </div>
            <div class="book-info">
                <h2 class="book-title">{{ libro.titulo }}</h2>
                <p class="book-author">
                    Autor: {{ libro.usuario.nombre_usuario }}
                </p>
                <p class="book-publ-date">
                    Publicado: {{ formatearFecha(libro.fecha_publicacion) }}
                </p>
                <div class="favorite-container">
                    <i class="fa-regular fa-heart" title="Añadir a favoritos"></i>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
    import { ref, onMounted } from 'vue';
    import axios from 'axios';
    import router from '@/router';

    const token = ref('');
    const tipoUsuario = ref('');
    const libros = ref([]);
    const page = ref(1);
    const size = ref(10);
    const total = ref(0);
    const hasNext = ref(false);

    // Obtener libros paginados
    async function getLibrosPaginados() {
        validarToken();

        try {
            const response = await axios.get('http://localhost:8000/libros', {
                headers: {
                    Authorization: `Bearer ${token.value}`,
                },
                params: {
                    page: page.value,
                    size: size.value,
                },
            });

            console.log(response.data)

            libros.value = response.data.data;
            total.value = response.data.total;
            hasNext.value = response.data.has_next;
        } catch (error) {
            console.error("Error al obtener libros:", error);
        }
    }

    async function getTipoUsuario() {
        validarToken();

        try {
            const response = await axios.get('http://localhost:8000/usuario/me/type', {
                headers: {
                    Authorization: `Bearer ${token.value}`,
                },
            });
            tipoUsuario.value = response.data;
        } catch (error) {
            console.error(error);
        }
    }

    const formatearFecha = (fecha) => {
        const date = new Date(fecha);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Meses empiezan desde 0
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('/');
            return;
        }
    }

    onMounted(() => {
        validarToken();
        getTipoUsuario();
        getLibrosPaginados();
    });
</script>

<style scoped>
    .header-section {
        width: 100%;
        display: flex;
        justify-content: space-between;
    }

    .scroll-container {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
        padding: 20px;
    }

    .book-card {
        width: 200px;
        border: 1px solid var(--color-border);
        border-radius: 12px;
        overflow: hidden;
        background-color: var(--background-color-primary);
        box-shadow: var(--box-shadow);
        transition: transform 0.3s;
    }

    .book-card:hover {
        transform: scale(1.05);
        cursor: pointer;
    }

    .image-container {
        overflow: hidden;
    }

    .book-image {
        width: 100%;
        height: 250px;
        object-fit: cover;
    }

    .book-info {
        padding: 10px;
        color: var(--color-text-primary);
    }

    .book-title {
        font-size: 16px;
        font-weight: bold;
        margin: 0 0 5px;
    }

    .book-author,
    .book-publ-date,
    .book-categories {
        font-size: 14px;
        margin: 0 0 5px;
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

    button {
        padding: 5px 10px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        background-color: green;
        color: white;
        font-weight: bold;
        transition: opacity 0.3s;
    }

    button:disabled {
        opacity: 0.5;
        cursor: not-allowed;
    }
</style>
