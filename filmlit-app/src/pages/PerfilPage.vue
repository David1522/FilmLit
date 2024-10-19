<template>
    <div class="perfil-main-container">
        <PerfilCard ref="perfilCard"/>
        <RouterView @perfil-updated="actualizarPerfil"/>
    </div>
</template>

<script setup>
    import PerfilCard from '@/components/perfil/PerfilCard.vue';
    import { ref } from 'vue';
    import { RouterView } from 'vue-router';
    
    const perfilCard = ref(null);

    async function actualizarPerfil() {
        if (perfilCard.value) {
            // Llama la funcion para volver a cargar la info del perfil del usuario
            await perfilCard.value.fetchPerfilUsuario(); 
        }
    }
</script>

<style scoped>
    .perfil-main-container {
        width: 65%;
        height: auto;
        background-color: var(--background-color-primary);
        color: var(--color-text-primary);
        padding-top: 30px;
        position: relative
    }

    @media (max-width: 800px) {
        .perfil-main-container {
            width: 100%;
        }
    }
</style>