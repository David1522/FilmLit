<template>
    <a href="/inicio" class="logo-container">
        <img src="../icons/FilmLit_Logo_Blanco-removebg-preview.png" alt="app-logo" class="app-logo" v-if="isDark">
        <img src="../icons/FilmLit_Logo_Morado-removebg-preview.png" alt="app-logo" class="app-logo" v-else>
    </a>
</template>

<script setup>
    import { ref } from 'vue'

    const isDark = ref(document.documentElement.className === 'dark');

    function updateTheme() {
        isDark.value = document.documentElement.className === 'dark';
    }

    document.addEventListener('DOMContentLoaded', () => {
        document.body.addEventListener('click', (event) => {
            if (event.target.classList.contains('theme-switcher-btn')) {
                updateTheme()
            }
        })
    })
</script>

<style scoped>
    .logo-container {
        width: 100%;
        height: 40px;
        display: flex;
        justify-content: center;
        align-items: center;
        margin-top: 15px;
        margin-bottom: 15px;
    }

    .app-logo {
        width: 40px;
        height: 40px;
    }

    @media (max-width: 770px) {
        .logo-container {
            display: none;
        }

        .app-logo {
            display: none;
        }
    }
</style>