<template>
    <div>
        <div class="button-container">
            <router-link class="link" :to = "{name: 'Peliculas'}">Peliculas</router-link>
            <router-link class="link" :to = "{name: 'Series'}">Series</router-link>
            <router-link class="link" :to = "{name: 'Libros'}">Libros</router-link>
        </div>
        <router-view /> <!-- Aquí se cargarán las vistas hijas -->
    </div>
</template>

<script>
import router from '@/router';
export default {
    name: 'Publicaciones'
};
</script>

<style scoped>
.button-container {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
}
.link {
        justify-self: end;
        width: 75px;
        height: 30px;
        background-color: var(--background-color-primary);
        color: var(--color-text-primary);
        font-weight: 600;
        font-size: 14px;
        border: 1px solid var(--color-border);
        border-radius: 15px;
        margin: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 5px;
        cursor: pointer;
        transition: background-color 200ms ease-in-out;
    }

    .link:hover {
        background-color: green;
        transition: background-color 200ms ease-in-out;
    }
</style>
