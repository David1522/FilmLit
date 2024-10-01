<template>
    <div class="main-container">
        <Header class="header"/>
        <AccountBar class="cuenta"/>
        <div class="contenido"></div>
    </div>
</template>

<script setup>
    import Header from '@/components/UI/Header.vue';
    import AccountBar from '@/components/UI/AccountBar.vue';

    import { ref, onMounted } from 'vue';
    import { useRouter } from 'vue-router';
    import axios from 'axios';

    const usuario = ref(null)

    const router = useRouter();
    
    async function authSession() {
        const token = localStorage.getItem('token');
        if (!token) {
            router.push('/login');
            return;
        }

        try {
            const response = await axios.get('http://127.0.0.1:8000/usuarios/me', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            usuario.value = response.data
        } catch (error) {
            console.log(error)
            router.push('/login')
        }
    }

    onMounted(() => {
        authSession();
    })
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