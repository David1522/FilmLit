<template>
    <header>
        <AppLogo :isDark="isDark"/>

        <nav class="nav-bar">
            <a href="/publicaciones"> <span class="icon"> <fa icon="home"/> </span> <span class="icon-text"> Inicio </span> </a>
            <a href="/biblioteca/peliculas"> <span class="icon"> <fa icon="bookmark"/> </span> <span class="icon-text"> Biblioteca </span> </a>
            <a href="/salas"> <span class="icon"> <fa icon="people-group"/> </span> <span class="icon-text"> Salas </span> </a>
            <a :href="`/perfil/${perfil?.id_perfil || ''}`"> <span class="icon"> <fa icon="user"/> </span> <span class="icon-text"> Perfil </span> </a>
            <a href="/noticias"> <span class="icon"> <fa icon="newspaper"/> </span> <span class="icon-text"> Noticias </span> </a>
            <a href="/premium"> <span class="icon"> <fa icon="star"/> </span> <span class="icon-text"> Premium </span> </a>

            <!-- Botones especiales -->
            <ThemeSwitch class="setting" :isDark="isDark" @toggle-theme="setTheme"/>
            <Settings class="setting"/>
        </nav>
    </header>
</template>

<script setup>
    import AppLogo from './AppLogo.vue';
    import ThemeSwitch from './ThemeSwitch.vue';
    import Settings from './Settings.vue';

    import { ref, onMounted } from 'vue';
    import axios from 'axios'; // Ensure axios is imported
    import router from '@/router';

    const token = ref('');

    const perfil = ref(null); // Holds profile data
    const isDark = ref(localStorage.getItem('theme') === 'dark');

    function setTheme() {
        const newTheme = isDark.value ? 'light' : 'dark';
        document.documentElement.className = newTheme;
        isDark.value = newTheme === 'dark';
        localStorage.setItem('theme', newTheme);
    }

    async function fetchPerfilUsuario() {
        validarToken();

        try {
            const response = await axios.get('http://localhost:8000/perfil/me', {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            });
            perfil.value = response.data; // Store response data in `perfil`
        } catch (error) {
            localStorage.removeItem('token');
            router.push('/');
        }
    }

    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('/');
            return;
        }
    }

    onMounted(() => {
        fetchPerfilUsuario();
        const theme = localStorage.getItem('theme');
        
        if (theme) {
            document.documentElement.className = theme;
        }
    });
</script>

<style scoped>
    header {
        background-color: var(--background-color-primary);
        border-right: 1px solid var(--color-border);
    }

    .nav-bar {
        width: 100%;
        height: auto;
        display: grid;
        grid-template: repeat(10, 40px) / 1fr;
        gap: 5px;
        padding: 10px
    }

    .nav-bar > a {
        color: var(--color-text-primary);
        border-radius: 15px;
        padding-left: 15px;
        display: flex;
        justify-content: start;
        align-items: center;
        gap: 15px;
    }

    .nav-bar > a:hover {
        background-color: var(--background-color-secondary);
    }

    .icon {
        width: 30px;
        height: 30px;
        font-size: 20px;
        display: flex;
        justify-content: center;
        align-items: center;
        transition: font-size ease 300ms;
    }

    a:hover .icon {
        font-size: 23px;
        transition: font-size ease 300ms;
    }

    @media (max-width: 800px) {
        .nav-bar {
            grid-template: 1fr / repeat(8, 1fr);
            width: 90%;
            margin: 0 auto;
        }

        .nav-bar > a {
            padding: 0;
            width: 30px;
            height: 30px;
            align-self: center;
        }

        .setting {
            align-self: center;
        }

        .icon {
            width: 100%;
        }

        .icon-text {
            display: none;
        }
    }
</style>