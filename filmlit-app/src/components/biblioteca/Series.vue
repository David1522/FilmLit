<template>
    <div class="scroll-container" @scroll="handleScroll">
        <h1 style="color: var(--color-text-primary); margin-bottom: 15px;">Series Populares</h1>

        <ul v-if="series" class="series-list">
            <li v-for="serie in series" :key="serie.id" class="series-card">
                <div class="image-container">
                    <img :src="'https://image.tmdb.org/t/p/w500' + serie.poster_path" alt="Poster" class="series-image"/>
                </div>

                <div class="series-info">
                    <h2 class="series-title">{{ serie.name }}</h2>
                    <div class="favorite-container">
                        <i class="fa-regular fa-heart" title="Añadir a favoritos"></i>
                    </div>
                </div>
            </li>
        </ul>

        <p v-if="!series.length && hasSearched" class="no-results">No se encontraron resultados.</p>
    </div>
</template>

<script setup>
    import { ref, onMounted, onBeforeUnmount } from 'vue';
    import axios from 'axios';

    const series = ref([]);
    const page = ref(1);
    const isLoading = ref(false);

    const getSeries = () => {
        if (isLoading.value) return;
        isLoading.value = true;
        axios.get(`https://api.themoviedb.org/3/tv/popular?api_key=efbd7d7c9ab8c82f03ba88c9ae89b34b&language=en-US&page=${page.value}`)
            .then(response => {
                series.value.push(...response.data.results);
                page.value += 1;
                isLoading.value = false;
            })
            .catch(error => {
                console.error('Error al obtener las series:', error);
                isLoading.value = false;
            });
    };

    const handleScroll = () => {
        const container = document.querySelector('.scroll-container');
        const inferiorContainer = container.scrollHeight - container.scrollTop <= container.clientHeight + 200;

        if (inferiorContainer) {
            getSeries();
        }
    };

    onMounted(() => {
        getSeries();
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

    .series-list {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-wrap: wrap;
        gap: 16px;
        list-style: none;
        padding: 10px;
        margin: 0;
    }

    .series-card {
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

    .series-card:hover {
        transform: scale(1.05);
        cursor: pointer;
    }

    .image-container {
        overflow: hidden;
    }

    .series-image {
        width: 100%;
        height: 350px;
        object-fit: cover;
    }

    .series-info {
        padding: 10px;
        color: var(--color-text-primary);
    }

    .series-title {
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
        .series-card {
            width: 100%;
            max-width: 250px;
        }
    }
</style>
