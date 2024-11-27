<template>
    <div class="btn-container" v-if="props.idPerfilCreador != idPerfil">
        <button v-if="!isFollow" class="follow-btn" @click="handlePostFollow">Seguir</button>
        <button v-else class="followed-btn" @click="handleDeleteFollow">Seguido</button>
    </div>
</template>

<script setup>
    import { defineProps, onMounted, ref, defineEmits } from 'vue';
    import axios from 'axios';
    import router from '@/router';

    const token = ref('');
    const idPerfil = ref(null);
    const isFollow = ref(false);

    const props = defineProps({
        idPerfilCreador: Number,
    });

    const emits = defineEmits(['update-follow-data']);

    async function getIdPerfil() {
        validarToken();

        try {
            const response = await axios.get("http://localhost:8000/perfil/me/id", {
                headers: { Authorization: `Bearer ${token.value}` },
            });
            idPerfil.value = response.data;
        } catch (error) {
            console.log(error);
        }
    }

    async function validateFollow() {
        validarToken();

        try {
            const response = await axios.get(`http://localhost:8000/perfil/me/follows/${props.idPerfilCreador}`, {
                headers: { Authorization: `Bearer ${token.value}` },
            });
            isFollow.value = response.data;
            console.log('Usuario seguido: ', isFollow.value);
        } catch (error) {
            console.log(error);
        }
    }

    async function postFollow() {
        validarToken();

        try {
            await axios.post(`http://localhost:8000/perfil/me/follows/${props.idPerfilCreador}`, {}, {
                headers: { Authorization: `Bearer ${token.value}` },
            });
            validateFollow();
            emits('update-follow-data'); // Emit the event after following
        } catch (error) {
            console.log(error);
        }
    }

    async function deleteFollow() {
        validarToken();

        try {
            await axios.delete(`http://localhost:8000/perfil/me/follows/${props.idPerfilCreador}`, {
                headers: { Authorization: `Bearer ${token.value}` },
            });
            validateFollow();
            emits('update-follow-data'); // Emit the event after unfollowing
        } catch (error) {
            console.log(error);
        }
    }

    // Wrappers to emit the event after the API calls
    function handlePostFollow() {
        postFollow();
    }

    function handleDeleteFollow() {
        deleteFollow();
    }

    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('/');
            return;
        }
    }

    onMounted(() => {
        getIdPerfil();
        validateFollow();
    });
</script>


<style scoped>
    .follow-btn {
        width: 75px;
        height: 30px;
        background-color: var(--background-color-contrast);
        color: var(--color-text-contrast);
        font-weight: 600;
        border: none;
        border-radius: 50px;
        cursor: pointer;
        transition: all 0.3s ease;
    }

    .followed-btn {
        width: 75px;
        height: 30px;
        background-color: grey;
        color: var(--color-text-contrast);
        font-weight: 600;
        border: none;
        border-radius: 50px;
        cursor: pointer;
        transition: all 0.3s ease;
    }
</style>