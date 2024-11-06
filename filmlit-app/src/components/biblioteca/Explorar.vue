<template>
    <div>
        <div class="button-container">
            <router-link :to = "{name: 'Peliculas'}">Peliculas</router-link>
            <router-link :to = "{name: 'Series'}">Series</router-link>
            <router-link :to = "{name: 'Libros'}">Libros</router-link>
        </div>
        <router-view /> <!-- Aquí se cargarán las vistas hijas -->
    </div>
</template>

<script>
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
button {
    padding: 10px 20px;
    font-size: 16px;
    cursor: pointer;
}
</style>
