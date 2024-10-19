<template>
    <div class="publicaciones-main-container">
        <CrearPublicacionBtn/>
        <PublicacionesCard ref="publicacionesCard"/>
        <RouterView @publicacion-creada="actualizarPost"/>
    </div>

</template>

<script setup>
    import CrearPublicacionBtn from '@/components/publicaciones/CrearPublicacionBtn.vue';
    import PublicacionesCard from '@/components/publicaciones/PublicacionesCard.vue';
    import { RouterView } from 'vue-router';
    import { ref } from 'vue';

    const publicacionesCard = ref(null)

    async function actualizarPost() {
        if (publicacionesCard.value)
        await publicacionesCard.value.updatePost();
    }
</script>

<style scoped>
    .publicaciones-main-container {
        width: 100%;
        max-width:650px;
        display: grid;
        grid-template: 60px 80vh / 1fr;
        position: relative;
    }

    @media (max-width: 800px) {
        .publicaciones-main-container {
            grid-template: 60px 69vh / 1fr;
        }
    }
</style>