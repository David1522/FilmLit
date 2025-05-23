<template>
    <div class="comentarios-container">
        <div class="comentario-card" v-for="comentario in comentarios" :key="comentario.id_comenario">
            <img :src="comentario.perfil.foto_perfil ? `http://localhost:8000/static/fotos_perfil/${comentario.perfil.foto_perfil}?${Date.now()}` : 'http://localhost:8000/static/fotos_perfil/pfp-icon.jpg'" class="pfp-usuario" alt="user-pfp">

            <div class="comentario-info">
                <span class="nombreusuario">{{ comentario.perfil.usuario.nombre_usuario }}</span>

                <p class="descripcion">{{ comentario.descripcion }}</p>

                <span class="fecha">{{ formattedDate(comentario.fecha) }}</span>
            </div>
            <opciones-comentario :isOwnComent="comentario.perfil.id_perfil == idPerfil" :idComentario="comentario.id_comentario" @comment-deleted="commentDeleted"/>
        </div>
    </div>
</template>

<script setup>
    import OpcionesComentario from './OpcionesComentario.vue';

    import router from '@/router';
    import axios from 'axios';
    import { onMounted, ref, defineEmits } from 'vue';
    import { useRoute } from 'vue-router';
    import { format } from 'date-fns';

    const route = useRoute();
    const token = ref('');

    const emits = defineEmits(['update-post']);

    const idPerfil = ref(null)
    const comentarios = ref([])

    // Variables para la paginacion de comentarios
    const idPublicacion = ref(route.params.id);
    const page = ref(1)
    const size = ref(10)
    const total = ref(0)
    const hasNext = ref(false)
    const cargandoComentarios = ref(false)

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

    async function getIdPerfil() {
        validarToken();

        try {
            const response = await axios.get("http://localhost:8000/perfil/me/id", {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            });
            idPerfil.value = response.data;
        } catch (error) {
            console.log(error);
        }
    }

    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('/');
            return;
        }
    }

    function commentDeleted() {
        actualizarComentarios();
        console.log("Emitting update-post event");
        emits("update-post");
    }

    defineExpose({
        actualizarComentarios,
        cargarComentarios,
    });


    onMounted(() => {
        getComentarios();
        getIdPerfil();
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
        width: 100%;
        display: flex;
        gap: 10px;
        border-top: 1px solid var(--color-border);
        border-bottom: 1px solid var(--color-border);
        padding: 10px 10px;
    }

    .pfp-usuario {
        width: 50px;
        height: 50px;
        border-radius: 50%;
    }

    .comentario-info {
        width: 80%;
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
        word-break: break-all;
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