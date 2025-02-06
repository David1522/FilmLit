<template>
    <div>
        <div class="button-container">
            <router-link class="link peliculas" :to = "{name: 'Peliculas'}">Peliculas</router-link>
            <router-link class="link series" :to = "{name: 'Series'}">Series</router-link>
            <router-link class="link libros" :to = "{name: 'Libros'}">Libros</router-link>
            <router-link class="link nuestros-autores" :to = "{name: 'LibrosAutores'}">Nuestros Autores</router-link>
        </div>
        <router-view class="router-container"/> <!-- Aquí se cargarán las vistas hijas -->
    </div>
</template>

<script setup>
    import router from '@/router';
</script>

<style scoped>
    .button-container {
        display: flex;
        gap: 10px;
        margin-bottom: 20px;
    }

    .link {
        color: var(--color-text-primary);
        height: 30px;
        border: 1px solid var(--color-text-primary);
        border-radius: 15px;
        padding: 0 15px;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: background-color 200ms ease-in-out;
    }

    .peliculas:hover {
        background-color: #FCC737;
        transition: background-color 200ms ease-in-out;
    }

    .series:hover {
        background-color: #F26B0F;
        transition: background-color 200ms ease-in-out;
    }

    .libros:hover {
        background-color: #E73879;
        transition: background-color 200ms ease-in-out;
    }

    .nuestros-autores:hover {
        background-color: #7E1891;
        transition: background-color 200ms ease-in-out;
    }

    .router-container {
        width: 100%;
    }
</style>
