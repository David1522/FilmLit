<template>
    <div class="main-container">
        <FormDecorator v-if="windowWidth > 650"/>
        <LoginForm/>
    </div>
</template>

<script setup>
    import FormDecorator from '@/components/login/FormDecorator.vue';
    import LoginForm from '@/components/login/LoginForm.vue';

    import { ref } from 'vue';

    const windowWidth = ref(window.innerWidth)

    window.addEventListener('resize', () => {
        windowWidth.value = window.innerWidth
    })
</script>

<style lang="scss" scoped>
    .main-container {
        width: 100%;
        height: 100vh;
        display: grid;
        grid: 1fr / 1fr 1fr;
    }

    @media (max-width: 650px) {
        .main-container {
            width: 100%;
            height: 100vh;
            display: grid;
            grid: 1fr / 1fr;
        }

        /* Añade estilos al contenedor del componente */
        .main-container > FormDecorator {
            display: none;
        }
    }
</style>