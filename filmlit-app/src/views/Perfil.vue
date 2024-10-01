<template>
    <div class="main-container">
        <Header class="header"/>
        <PerfilPage class="contenido"/>
    </div>
</template>

<script setup>
    import Header from '@/components/UI/Header.vue';
    import PerfilPage from '@/pages/PerfilPage.vue';
</script>

<style scoped>
    .main-container {
        width: 100%;
        height: 100vh;
        display: grid;
        grid-template:  1fr / 250px 1fr;
        grid-template-areas: 
            "header contenido"
            "header contenido";
    }

    .header {
        grid-area: header;
    }

    .contenido {
        grid-area: contenido;
        justify-self: center;
    }

    @media (max-width: 800px) {
        .main-container {
            grid-template: 1fr 90px / 1fr;
            grid-template-areas: 
                "contenido"
                "header";
        }

        .contenido {
            justify-self: flex-start;
        }
    }
</style>