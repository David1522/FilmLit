<template>
    <form @submit.prevent="publicarComentario" class="form-container">
        <label for="comentario" class="input-container">
            <textarea name="descripcion-comentario" id="comentario" class="comentario-input" placeholder="Añade un comentario..." minlength="1" maxlength="280" v-model="descripcion"></textarea>
        </label>

        <button type="submit" class="form-btn">Post</button>
    </form>
</template>

<script setup>
    import router from '@/router';
    import axios from 'axios';
    import { ref } from 'vue';
    import { defineEmits } from 'vue';
    import { useRoute } from 'vue-router';
    
    const route = useRoute();
    const emit = defineEmits(['comentario-creado']);

    const token = ref('');
    const idPublicacion = ref(route.params.id)
    const descripcion = ref('');


    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('/login');
            return;
        }
    }


    async function publicarComentario() {
        validarToken();

        try {
            const response = await axios.post(
                `http://localhost:8000/publicaciones/${idPublicacion.value}/comentario`, {},
                {
                    headers: {
                        Authorization: `Bearer ${token.value}`,
                    },
                    params: {
                        descripcion: descripcion.value,
                    }
                }
            )
            descripcion.value = "";
            emit('comentario-creado')
        } catch (error) {
            console.log(error);
            localStorage.removeItem('token');
            router.push('/login');
        }
    }
</script>

<style scoped>
    .form-container {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
    }

    .input-container {
        width: 100%;
    }

    .comentario-input {
        width: 100%;
        background-color: var(--background-color-primary);
        color: var(--color-text-primary);
        min-height: 0px;
        height: auto;
        padding: 0 15px;
        border: 1px solid var(--color-border);
        border-radius: 5px;
        resize: none;
        padding-top: 10px; 
    }

    .form-btn {
        width: 75px;
        height: 30px;
        background-color: var(--background-color-contrast);
        color: var(--color-text-contrast);
        font-weight: 600;
        border: none;
        border-radius: 50px;
        cursor: pointer;
    }
</style>