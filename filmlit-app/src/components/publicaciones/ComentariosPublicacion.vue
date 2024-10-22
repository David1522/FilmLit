<template>
    <div class="comentarios-container">
        <div class="comentario-card" v-for="comentario in comentarios" :key="comentario.id_comenario">
            <img src="../icons/pfp-icon.jpg" alt="user-pfp" class="pfp-usuario">

            <div class="comentario-info">
                <span class="nombreusuario">{{ comentario.perfil.usuario.nombre_usuario }}</span>

                <p class="descripcion">{{ comentario.descripcion }}</p>

                <span class="fecha">{{ formattedDate(comentario.fecha) }}</span>
            </div>
            <div class="icon-container">
                    <fa icon="ellipsis"/>
            </div>
        </div>
    </div>
</template>

<script setup>
    import router from '@/router';
    import axios from 'axios';
    import { onMounted, ref } from 'vue';
    import { useRoute } from 'vue-router';
    import { format } from 'date-fns';

    const route = useRoute();
    const emit = defineEmits(['childCliked']);
    const token = ref('');

    const comentarios = ref([])
    

    // Variables para la paginacion de comentarios
    const idPublicacion = ref(route.params.id);
    const page = ref(1)
    const size = ref(10)
    const total = ref(0)
    const hasNext = ref(false)
    const cargandoComentarios = ref(false)


    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('/login');
            return;
        }
    }


    async function getComentarios(page = 1) {
        validarToken();

        try {
            const response = await axios.get(`http://localhost:8000/publicaciones/${idPublicacion.value}/comentarios`, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                },
                params: {
                    page,
                    size: size.value,
                }
            });

            const { data, total: totalComentarios, has_next } = response.data;

            comentarios.value = [...comentarios.value, ...data];
            total.value = totalComentarios;
            hasNext.value = has_next;

            console.log(comentarios)
        } catch (error) {
            console.log(error)
        }
    }


    async function actualizarComentarios() {
        comentarios.value = [];
        page.value = 1;
        getComentarios();
    }


    async function cargarComentarios() {
        if (!hasNext.value || cargandoComentarios.value) return;

        cargandoComentarios.value = true;
        page.value += 1;

        getComentarios(page.value);
        cargandoComentarios.value = false;
    }


    const formattedDate = (date) => {
        return format(new Date(date), "h:mm aaa · MMM d, yyyy");
    };


    defineExpose({
        actualizarComentarios,
        cargarComentarios,
    });


    onMounted(() => {
        getComentarios();
    })

</script>

<style scoped>
    .comentarios-container {
        width: 100%;
        overflow: auto;
        scrollbar-width: none;
        -ms-overflow-style: none;
    }

    .comentario-card {
        display: flex;
        gap: 10px;
        border-top: 1px solid var(--color-border);
        border-bottom: 1px solid var(--color-border);
        padding: 10px 10px;
    }

    .pfp-usuario {
        width: 40px;
        height: 40px;
        border-radius: 50%;
    }

    .comentario-info {
        width: 100%;
        display: flex;
        flex-direction: column;
        padding-top: 9px;
        gap: 5px;
    }

    .nombreusuario {
        font-size: 16px;
        font-weight: 600;
    }

    .descripcion {
        width: 100%;
        font-size: 15px;
        font-weight: 100;
    }

    .fecha {
        font-size: 14px;
        font-weight: 100;
        color: var(--color-text-secundary);
    }

    .icon-container {
        width: 35px;
        height: 30px;
        color: var(--color-text-primary);
        font-size: 18px;
        border: none;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .icon-container:hover {
        background-color: var(--background-color-secondary);
        cursor: pointer;
    }
</style>