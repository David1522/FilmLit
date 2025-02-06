<template>
    <div class="scroll-container">
        <h1 style="color: var(--color-text-primary);">Películas Populares</h1>
        <ul v-if="peliculas" class="movie-list">
            <li v-for="pelicula in peliculas" :key="pelicula.id" class="movie-card">
                <div class="image-container">
                    <img :src="'https://image.tmdb.org/t/p/w500' + pelicula.poster_path" alt="Poster" class="movie-image"/>
                </div>
                <div class="movie-info">
                    <h2 class="movie-title">{{ pelicula.title }}</h2>
                    <i class="fa-regular fa-heart"></i>
                </div>
            </li>
        </ul>
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
        if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 500) {
            getPeliculas();
        }
    };

    // Función de debounce
    const debounce = (fn, delay) => {
        let timeout;
        return (...args) => {
            if (timeout) clearTimeout(timeout);
            timeout = setTimeout(() => fn(...args), delay);
        };
    };

    const debouncedScroll = debounce(handleScroll, 200);

    onMounted(() => {
        getPeliculas();
        window.addEventListener('scroll', debouncedScroll);
    });

    onBeforeUnmount(() => {
        window.removeEventListener('scroll', debouncedScroll);
    });
</script>

<style scoped>
    .movie-list {
        display: flex;
        flex-wrap: wrap;
        gap: 16px;
        list-style: none;
        padding: 0;
    }

    .movie-card {
        position: relative;
        width: 200px;
        border-radius: 12px;
        overflow: hidden;
        box-shadow: var(--box-shadow);
        display: flex;
        flex-direction: column;
        background-color: var(--background-color-primary);
    }

    .image-container {
        flex-grow: 1;
        overflow: hidden;
    }

    .movie-image {
        width: 100%;
        height: auto;
    }

    .movie-info {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 8px;
        background-color: var(--background-color-primary);
        color: var(--color-text-primary);
    }

    .fa-regular {
        font-size: 20px;
        font-weight: 500;
        border: 1px solid var(--color-text-primary);
        border-radius: 50%;
        padding: 5px;
    }
    .fa-regular:hover{
        cursor: pointer;
        font-weight: 600;
    }

    .movie-title {
        font-size: 16px;
        font-weight: bold;
        margin: 0;
    }
    .load-more-btn {
        display: block;
        margin: 20px auto;
        padding: 10px 20px;
        font-size: 16px;
        cursor: pointer;
        background-color: var(--background-color-primary);
        color: var(--color-text-primary);
        border: none;
        border-radius: 8px;
    }
    .scroll-container {
        overflow-y: auto;
    }
</style>
