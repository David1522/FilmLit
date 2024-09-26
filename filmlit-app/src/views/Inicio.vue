<template>
    <div class="main-container">
        <Header class="header"/>
        <div class="cuenta"></div>
        <div class="contenido"></div>
    </div>
</template>

<script setup>
    import Header from '@/components/UI/Header.vue';
</script>

<style scoped>
    .main-container {
        width: 100%;
        height: 100vh;
        display: grid;
        grid-template:  100px 1fr / 1fr 4fr 2fr;
        grid-template-areas: 
            "header cuenta cuenta"
            "header contenido contenido";
    }

    .cuenta {
        background-color: purple;
        grid-area: cuenta;
    }

    .contenido {
        background-color: yellow;
        grid-area: contenido;
    }
</style>