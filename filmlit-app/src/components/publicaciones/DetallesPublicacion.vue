<template>
    <div class="modal-detalles-publicacion">
            <div class="publicacion-contenedor" v-if="publicacion.perfil && publicacion.perfil.usuario" @scroll="handleScroll">
                <div class="publ-controls">
                <div class="publ-nav">
                    <div class="icon-container" @click="router.push('/publicaciones')">
                        <fa icon="arrow-left" class="go-back-btn"/>
                    </div>
                    <p>Post</p>
                </div>
                <div class="icon-container">
                    <fa icon="ellipsis"/>
                </div>
            </div>

            <div class="publ-header">
                <div class="info-usuario">
                    <img src="../icons/pfp-icon.jpg" alt="user-pfp" class="pfp-usuario">
                    <p class="nombre-usuario"> {{ publicacion.perfil.usuario.nombre_usuario }} </p>
                </div>
                
                <button class="follow-btn">Follow</button>
            </div>

            <div class="publ-contenido">
                <p class="publ-descripcion"> {{ publicacion.descripcion }} </p>
                <img src="../icons/img-post-example.jpg" alt="publ-img" class="publ-img">
            </div>

            <div class="publ-footer">
                <p class="publ-fecha"> {{ formattedDate(publicacion.fecha) }} </p>

                <div class="publ-stats" v-if="!interacciones.cargando">
                    <button class="publ-f-btn" :class="{ liked: interacciones.publicacionLikeada }" @click="likeFunc()">
                        <span> {{ interacciones.likes }} </span>
                        <fa icon="heart"/>
                    </button>

                    <button class="publ-f-btn">
                        <span> {{ interacciones.comentarios }} </span>
                        <fa icon="comment"/>
                    </button>
                </div>
                <div v-else>Cargando Interacciones...</div>
            </div>

            <div class="comentario-form">
                <CrearComentario @comentario-creado="actualizarPublicacion"/>
            </div>
            <div class="seccion-comentarios">
                <ComentariosPublicacion ref="comentariosPublicacion"/>
            </div>
        </div>
        <div v-else>Cargando Publicacion...</div>
    </div>
</template>

<script setup>
    import CrearComentario from './CrearComentario.vue';
    import ComentariosPublicacion from './ComentariosPublicacion.vue';

    import router from '@/router';
    import axios from 'axios';
    import { ref, onMounted } from 'vue';
    import { useRoute } from 'vue-router';
    import { format } from 'date-fns';


    const route = useRoute();
    const token = ref('');
    const comentariosPublicacion = ref(null)

    const idPublicacion = ref(route.params.id);
    const publicacion = ref({})
    const interacciones = ref({})


    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('/login');
            return;
        }
    }


    const formattedDate = (date) => {
        return format(new Date(date), "h:mm aaa · MMM d, yyyy");
    };


    async function fetchPublicacion() {
        validarToken();

        try {
            const response = await axios.get(`http://localhost:8000/publicaciones/${idPublicacion.value}`, {
                headers: {
                    Authorization: `Bearer ${token.value}`,
                }
            });
            publicacion.value = response.data;
            getNumInteracionesPublicacion();
        } catch (error) {
            console.log(error);
            localStorage.removeItem('token');
            router.push('/login');
        }
    }


    async function getNumInteracionesPublicacion() {
        interacciones.value = { "cargando": true }
        try {
            const response = await axios.get(`http://localhost:8000/publicaciones/${idPublicacion.value}/interacciones`, {
                headers: {
                    Authorization: `Bearer ${token.value}`,
                },
            });
            interacciones.value = {
                "cargando": false,
                "likes": response.data.likes || 0,
                "comentarios": response.data.comentarios || 0,
                "publicacionLikeada": response.data.publicacionLikeada || false,
            }
        } catch (error) {
            console.log(error);
            localStorage.removeItem('token');
            router.push('/login');
        }
    }


    async function likeFunc() {
        const statusLikePrevio = interacciones.value.publicacionLikeada;

        // Actualziacion visual de likes
        const interaccionActual = interacciones.value;
        interaccionActual.publicacionLikeada = !statusLikePrevio;
        interaccionActual.likes += statusLikePrevio ? -1 : 1;

        try {
            if (statusLikePrevio) {
                await axios.delete(`http://localhost:8000/publicaciones/${idPublicacion.value}/like`, {
                    headers: {
                        Authorization: `Bearer ${token.value}`,
                    }
                })
            } else {
                await axios.post(`http://localhost:8000/publicaciones/${idPublicacion.value}/like`, {}, {
                    headers: {
                        Authorization: `Bearer ${token.value}`,
                    }
                });
            }
        } catch (error) {
            // Revierte la actualizacion visual
            interaccionActual.publicacionLikeada = statusLikePrevio;
            interaccionActual.likes -= statusLikePrevio ? -1 : 1;
            console.log(error);
        }
    }

    async function actualizarPublicacion() {
        getNumInteracionesPublicacion();
        if (comentariosPublicacion.value) {
            await comentariosPublicacion.value.actualizarComentarios();
        }
    }

    async function cargarComentarios() {
        if (comentariosPublicacion.value) {
            await comentariosPublicacion.value.cargarComentarios();
        }
    }


    function handleScroll() {
        const container = document.querySelector('.publicacion-contenedor');
        const inferiorContainer = container.scrollHeight - container.scrollTop <= container.clientHeight + 200;

        if (inferiorContainer) {
            cargarComentarios();
        }
    }


    onMounted(() => {
        fetchPublicacion();
    })
</script>

<style scoped>
    .modal-detalles-publicacion {
        position: absolute;
        top: 0px;
        width: 100%;
        height: 100%;
        background-color: var(--background-color-primary);
        display: flex;
        flex-direction: column;
        align-items: center;
        color: white;
    }

    .publicacion-contenedor {
        width: 100%;
        max-width: 550px;
        height: 100%;
        background-color: var(--background-color-primary);
        color: var(--color-text-primary);
        padding: 0 18px;
        overflow: auto;
        scrollbar-width: none;
        -ms-overflow-style: none;
    }

    .publ-controls {
        width: 100%;
        height: 50px;
        background-color: var(--background-color-blur);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 18px;
        position: sticky;
        top: 0;
    }

    .publ-nav {
        display: flex;
        align-items: center;
        gap: 30px;
    }

    .go-back-btn {
        font-size: 18px;
    }

    .publ-nav > p {
        color: var(--color-text-primary);
        font-size: 20px;
        font-weight: bold;
    }

    .icon-container {
        width: 30px;
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

    .publ-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;
        padding: 0 10px;
    }

    .pfp-usuario {
        width: 40px;
        height: 40px;
        border-radius: 50%;
    }

    .info-usuario {
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .nombre-usuario {
        font-size: 16px;
        font-weight: 600;
    }

    .follow-btn {
        width: 75px;
        height: 30px;
        background-color: var(--background-color-contrast);
        color: var(--color-text-contrast);
        font-weight: 600;
        border: none;
        border-radius: 50px;
        cursor: pointer;
    }

    .publ-contenido {
        display: flex;
        flex-direction: column;
        justify-content: center;
        gap: 10px;
        padding: 0 10px;
    }

    .publ-descripcion {
        font-size: 15px;
        font-weight: 100;
    }

    .publ-img {
        width: 100%;
        border-radius: 18px;
    }

    .publ-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 10px;
    }

    .publ-fecha {
        font-weight: 100;
        color: var(--color-text-secundary);
        margin-top: 15px;
    }

    .publ-stats {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 12px;
        padding-right: 15px;
    }

    .publ-f-btn {
        background-color: transparent;
        color: var(--color-text-primary);
        font-size: 18px;
        border: none;
        cursor: pointer;
        transition: color 200ms ease-in-out;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 5px;
    }

    .publ-f-btn > span {
        color: var(--color-text-primary);
        font-size: 14px;
    }

    .publ-f-btn:hover {
        color: var(--color-text-secundary);
        transition: color 200ms ease-in-out;
    }

    .publ-f-btn.liked {
        color: red;
    }

    .publ-f-btn.liked:hover {
        color: rgb(183, 0, 0);
    }

    /* Aquí va el componente del form de comentario */
    .comentario-form {
        width: 100%;
        border-top: 1px solid var(--color-border);
        border-bottom: 1px solid var(--color-border);
        padding: 10px;
    }

    .seccion-comentarios {
        border-bottom: 1px solid var(--color-border);
    }
</style>