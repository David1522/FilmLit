<template>
    <div class="cuenta-main-container" v-if="perfil">
        <div class="credentials-container">
            <a href="/perfil" class="nombre-usuario">{{ perfil.usuario.nombre_usuario }}</a>
            <p class="perfil-nombre">{{ perfil.nombre }}</p>
        </div>
        <a href="./perfil"><img :src="perfil.foto_perfil ? `http://localhost:8000/static/fotos_perfil/${perfil.id_perfil}.jpg`
            : 'http://localhost:8000/static/fotos_perfil/pfp-icon.jpg'" alt="pfp-usuario" class="pfp-usuario"></a>
    </div>
    <div v-else>
        <p>Cargando Cuetna...</p>
    </div>
</template>

<script setup>
    import { ref, onMounted } from 'vue';
    import axios from 'axios';
    import router from '@/router';

    const perfil = ref(null);

    async function fetchCuentaUsuario() {
        const token = localStorage.getItem('token');
        if (!token) {
            router.push('/login')
            return
        }

        try {
            const response = await axios.get('http://127.0.0.1:8000/perfil/me', {
                headers: {
                    Authorization: `Bearer ${token}`,
                }
            })
            perfil.value = response.data
        } catch (error) {
            console.log(error);
            localStorage.removeItem('token');
            router.push('/login');
        }
    }

    onMounted(() => {
        fetchCuentaUsuario();
    })
</script>

<style scoped>
    .cuenta-main-container {
        color: var(--color-text-primary);
        border-bottom: 1px solid var(--color-border);
        display: flex;
        justify-content: end;
        align-items: center;
        gap: 15px;
        padding: 0 100px;
    }

    .credentials-container {
        font-size: 15px;
    }

    .nombre-usuario {
        color: var(--color-text-primary);
        font-weight: 600;
    }

    .perfil-nombre {
        color: var(--color-text-secundary);
    }

    .pfp-container {
        background-color: purple;
        height: 100%;
    }

    .pfp-usuario {
        width: 50px;
        height: 50px;
        border-radius: 50%;
    }

    @media (max-width: 800px) {
        .cuenta-main-container {
            padding: 0 21px;
        }
    }
</style>