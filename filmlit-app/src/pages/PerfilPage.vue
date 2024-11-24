<template>
    <div class="perfil-main-container">
        <PerfilCard ref="perfilCard"/>
        <PublicacionesPerfil ref="publicacionesPerfil"/>
    </div>
</template>

<script setup>
    import PerfilCard from '@/components/perfil/PerfilCard.vue';
    import PublicacionesPerfil from '@/components/perfil/PublicacionesPerfil.vue';
    import { ref } from 'vue';
    
    const perfilCard = ref(null);
    const publicacionesPerfil = ref(null)

    async function actualizarPerfil() {
        if (perfilCard.value && publicacionesPerfil.value) {
            // Llama la funcion para volver a cargar la info del perfil del usuario
            await perfilCard.value.fetchPerfilUsuario();
            await publicacionesPerfil.value.updatePost(); 
        }
    }
</script>

<style scoped>
    .perfil-main-container {
        width: 50%;
        height: 100vh;
        background-color: var(--background-color-primary);
        color: var(--color-text-primary);
        padding-top: 30px;
        position: relative;
        overflow: hidden; /* Prevent overflow beyond the viewport */
    }

    @media (max-width: 1200px) {
        .perfil-main-container {
            width: 100%;
        }
    }
</style>