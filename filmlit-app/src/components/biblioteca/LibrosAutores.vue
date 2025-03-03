<template>
    <div class="scroll-container" @scroll="handleScroll">
        <div class="header-section">
            <h2>Libros de Nuestros Autores</h2>
            <button v-if="tipoUsuario === 'AUTOR'" @click="router.push('/biblioteca/crear-libro')" class="btn-crear">Añadir Obra</button>
        </div>

        <div class="libros-container">
            <div v-for="libro in libros" :key="libro.id_libro" class="book-card">
                <div class="image-container">
                <img 
                    :src="`http://localhost:8000/static/libros/${libro.portada}?${Date.now}`" 
                    alt="Portada del libro" 
                    class="book-image" 
                    @click="router.push({ name: 'DetallesLibro', params: { idBook: libro.id_libro } });"
                />
                </div>

                <div class="book-info">
                    <div class="book-options">
                        <h2 class="book-title" @click="router.push({ name: 'DetallesLibro', params: { idBook: libro.id_libro } });">{{ libro.titulo }}</h2>
                        <OpcionesLibroAutores :idBook="libro.id_libro" @bookDeleted="getLibrosPaginados"/>
                    </div>
                    

                    <p class="book-author">
                        <span clas="property">Autor:</span> {{ libro.usuario.nombre_usuario }}
                    </p>
                    <p class="book-publ-date">
                        <span clas="property">Publicado:</span> {{ formatearFecha(libro.fecha_publicacion) }}
                    </p>

                    <div class="favorite-container">
                        <i class="fa-regular fa-heart" title="Añadir a favoritos"></i>
                    </div>
                </div>
            </div>
        </div>

        <div v-if="cargandoLibros" class="publ-message">Cargando Más Libros...</div>
        <div v-else class="publ-message">No hay más libros</div>
    </div>
</template>

<script setup>
    import OpcionesLibroAutores from './OpcionesLibroAutores.vue';

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
    const cargandoLibros = ref(false)

    // Obtener libros paginados
    async function getLibrosPaginados(page = 1) {
        validarToken();

        console.log("Cargando libros...")

        try {
            const response = await axios.get('http://localhost:8000/libros', {
                headers: {
                    Authorization: `Bearer ${token.value}`,
                },
                params: {
                    page: page,
                    size: size.value,
                },
            });

            console.log(response.data)

            libros.value = [...libros.value, ...response.data.data]; 
            total.value = response.data.total;
            hasNext.value = response.data.has_next;
        } catch (error) {
            console.error("Error al obtener libros:", error);
            router.push('/');
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
            router.push('/');
        }
    }

    const formatearFecha = (fecha) => {
        const date = new Date(fecha);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0'); // Meses empiezan desde 0
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    function handleScroll() {
        const container = document.querySelector('.scroll-container');
        const inferiorContainer = container.scrollHeight - container.scrollTop <= container.clientHeight + 200;

        if (inferiorContainer && hasNext.value) {
            cargarLibros();
        }
    }

    async function cargarLibros() {
        if (!hasNext.value || cargandoLibros.value) return;

        console.log("Pasa validacion")

        cargandoLibros.value = true;
        page.value += 1;

        console.log("Get libros paginados")
        await getLibrosPaginados(page.value);
        cargandoLibros.value = false;
    }

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

    .header-section > h2 {
        color: var(--color-text-primary);
        margin-bottom: 15px;
    }

    .scroll-container {
        width: 100%;
        height: calc(100vh - 190px);
        display: flex;
        flex-direction: column;
        padding: 20px;
        gap: 10px;
        overflow: auto;
        scrollbar-width: none;
        -ms-overflow-style: none;
    }

    .libros-container {
        width: 100%;
        height: auto;
        display: flex;
        flex-wrap: wrap;
        justify-content: center;
        gap: 20px;
    }

    .publ-message {
        align-self: center;
        color: var(--color-text-secundary);
        font-size: 14px;
    }

    .book-card {
        width: 100%;
        max-width: 300px;
        height: 450px;
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

    .book-options {
        display: flex;
        justify-content: space-between;
        align-items: start;
    }

    .book-title {
        font-size: 16px;
        font-weight: bold;
        margin: 0 0 5px;
    }

    .book-title:hover {
        text-decoration: underline;
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

    @media (max-width: 800px) {
        .scroll-container {
            height: calc(100vh - 355px);
        }
    }

    @media (max-width: 1030px) {
        .book-card {
            width: 100%;
            max-width: 250px;
        }
    }
</style>