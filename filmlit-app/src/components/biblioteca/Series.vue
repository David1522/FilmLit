<template>
    <div class="scroll-container">
        <h1 style="color: var(--color-text-primary);">Series Populares</h1>
        <ul v-if="series" class="series-list">
            <li v-for="serie in series" :key="serie.id" class="series-card">
                <div class="image-container">
                    <img :src="'https://image.tmdb.org/t/p/w500' + serie.poster_path" alt="Poster" class="series-image"/>
                </div>
                <div class="series-info">
                    <span class="like-icon">❤️</span>
                    <h2 class="series-title">{{ serie.name }}</h2>
                </div>
            </li>
        </ul>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            series: [],
            page: 1,
            isLoading: false,
        };
    },
    mounted() {
        this.getSeries();
        this.debouncedScroll = debounce(this.handleScroll, 200); // Usamos debounce con un retraso de 200 ms
        window.addEventListener('scroll', this.debouncedScroll);
    },
    beforeDestroy() {
        window.removeEventListener('scroll', this.debouncedScroll);
    },
    methods: {
        getSeries() {
            if (this.isLoading) return;
            this.isLoading = true;
            axios.get(`https://api.themoviedb.org/3/tv/popular?api_key=efbd7d7c9ab8c82f03ba88c9ae89b34b&language=en-US&page=${this.page}`)
                .then(response => {
                    this.series.push(...response.data.results);
                    this.page += 1;
                    this.isLoading = false;
                })
                .catch(error => {
                    console.error('Error al obtener las series:', error);
                    this.isLoading = false;
                });
        },
        handleScroll() {
            if ((window.innerHeight + window.scrollY) >= document.body.offsetHeight - 500) {
                this.getSeries();
            }
        }
    },
    name: 'Series'
};

// Función de debounce
function debounce(fn, delay) {
    let timeout;
    return function(...args) {
        if (timeout) clearTimeout(timeout);
        timeout = setTimeout(() => {
            fn(...args);
        }, delay);
    };
}
</script>

<style scoped>
.scroll-container {
    overflow-y: auto;
}

.series-list {
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
    list-style: none;
    padding: 0;
}

.series-card {
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

.series-image {
    width: 100%;
    height: auto;
}

.series-info {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 8px;
    background-color: var(--background-color-primary);
    color: var(--color-text-primary);
    flex-direction: column;
}

.like-icon {
    font-size: 20px;
    color: red;
}

.series-title {
    font-size: 16px;
    font-weight: bold;
    margin: 0;
}
</style>
