<template>
    <div class="scroll-container" @scroll="handleScroll">
        <h1 style="color: var(--color-text-primary); margin-bottom: 15px;">Películas Populares</h1>

        <ul v-if="peliculas" class="movie-list">
            <li v-for="pelicula in peliculas" :key="pelicula.id" class="movie-card">
                <div class="image-container">
                    <img :src="'https://image.tmdb.org/t/p/w500' + pelicula.poster_path" alt="Poster" class="movie-image"/>
                </div>

                <div class="movie-info">
                    <h2 class="movie-title">{{ pelicula.title }}</h2>
                    <div class="favorite-container">
                        <i class="fa-regular fa-heart" title="Añadir a favoritos"></i>
                    </div>
                </div>
            </li>
        </ul>

        <p v-if="!peliculas.length && hasSearched" class="no-results">No se encontraron resultados.</p>
    </div>
</template>

<script setup>
    import { ref, onMounted, onBeforeUnmount } from 'vue';
    import axios from 'axios';

    const peliculas = ref([]);
    const page = ref(1);
    const isLoading = ref(false);

    const getPeliculas = () => {
        if (isLoading.value) return;
        isLoading.value = true;
        axios.get(`https://api.themoviedb.org/3/movie/popular?api_key=efbd7d7c9ab8c82f03ba88c9ae89b34b&language=es-US&page=${page.value}`)
            .then(response => {
                peliculas.value.push(...response.data.results);
                page.value += 1;
                isLoading.value = false;
            })
            .catch(error => {
                console.error('Error al obtener las películas:', error);
                isLoading.value = false;
            });
    };

    const handleScroll = () => {
        const container = document.querySelector('.scroll-container');
        const inferiorContainer = container.scrollHeight - container.scrollTop <= container.clientHeight + 200;

        if (inferiorContainer) {
            getPeliculas();
        }
    };

    onMounted(() => {
        getPeliculas();
    });
</script>

<style scoped>
    .scroll-container {
        width: 100%;
        height: calc(100vh - 190px);
        overflow-y: auto;
        box-sizing: border-box;
        scrollbar-width: none;
    }

    .scroll-container::-webkit-scrollbar {
        display: none; /* Chrome, Safari, Edge */
    }

    .movie-list {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-wrap: wrap;
        gap: 16px;
        list-style: none;
        padding: 10px;
        margin: 0;
    }

    .movie-card {
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

    .movie-card:hover {
        transform: scale(1.05);
        cursor: pointer;
    }

    .image-container {
        overflow: hidden;
    }

    .movie-image {
        width: 100%;
        height: 350px;
        object-fit: cover;
    }

    .movie-info {
        padding: 10px;
        color: var(--color-text-primary);
    }

    .movie-title {
        font-size: 16px;
        font-weight: bold;
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

    .navbar {
        position: fixed;
        bottom: 0;
        width: 100%;
        height: 80px;
        background-color: var(--background-color-primary);
        z-index: 1000; /* Más alto que el contenido */
    }

    @media (max-width: 800px) {
        .scroll-container {
            height: calc(100vh - 355px);
        }
    }

    @media (max-width: 1030px) {
        .movie-card {
            width: 100%;
            max-width: 250px;
        }
    }
</style>
