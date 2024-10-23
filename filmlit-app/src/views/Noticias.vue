<template>
    <div class="main-container">
        <Header class="header"/>
        <AccountBar class="cuenta"/>
        <CargarNoticias class="contenido"/>

    </div>
</template>

<script setup>
    import Header from '@/components/UI/Header.vue';
    import AccountBar from '@/components/UI/AccountBar.vue';
    import CargarNoticias from '@/components/noticias/CargarNoticias.vue';
</script>
<style scoped>
    .main-container {
        width: 100%;
        height: 100vh;
        display: grid;
        grid-template:  80px 1fr / 250px 1fr;
        grid-template-areas: 
            "header cuenta cuenta"
            "header contenido contenido";
    }

    .header {
        grid-area: header;
    }

    .cuenta {
        grid-area: cuenta;
    }

    .contenido {
        grid-area: contenido;
        justify-self: center;
    }

    @media (max-width: 800px) {
        .main-container {
            grid-template: 100px 1fr 90px / 1fr;
            grid-template-areas: 
                "cuenta"
                "contenido"
                "header";
        }
    }
</style>