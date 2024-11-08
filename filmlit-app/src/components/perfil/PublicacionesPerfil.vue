<template>
    <div class="publicaciones-container" @scroll="handleScroll" v-if="publicaciones.length > 0">
        <div class="publ-card" v-for="publicacion in publicaciones" :key="publicacion.id_publicacion">
            <div class="publ-header">
                <div class="info-usuario">
                    <img :src="publicacion.perfil.foto_perfil ? `http://localhost:8000/static/fotos_perfil/${publicacion.perfil.id_perfil}.jpg`
            : 'http://localhost:8000/static/fotos_perfil/pfp-icon.jpg'" alt="usuario-pfp" class="pfp-usuario">
                    <p class="nombre-usuario">{{ publicacion.perfil.usuario.nombre_usuario }}</p>
                </div>


                <div class="publ-actions">
                    <div class="icon-container">
                        <fa icon="ellipsis"/>
                    </div>
                </div>
            </div>

            <div class="publ-content" @click="accederDetallesPublicacion(publicacion.id_publicacion)">
                <p>{{ publicacion.descripcion }}</p>
            </div>

            <div class="publ-footer">
                <p class="publ-content-date"> {{ formattedDate(publicacion.fecha) }} </p>
                
                <div class="publ-btns" v-if="!interaccionesPublicaciones[publicacion.id_publicacion]?.cargando">
                    <button 
                        class="publ-f-btn"
                        :class="{ liked: interaccionesPublicaciones[publicacion.id_publicacion]?.publicacionLikeada }"
                        @click="likeFunc(publicacion.id_publicacion, interaccionesPublicaciones[publicacion.id_publicacion].publicacionLikeada)">
                        <span> {{ interaccionesPublicaciones[publicacion.id_publicacion].likes }} </span>
                        <fa icon="heart"/>
                    </button>

                    <button class="publ-f-btn" @click="accederDetallesPublicacion(publicacion.id_publicacion)">
                        <span> {{ interaccionesPublicaciones[publicacion.id_publicacion].comentarios }} </span>
                        <fa icon="comment"/>
                    </button>
                </div>
                <div class="loading-message" v-else>Cargando Interacciones...</div>

                <a class="publ-comentarios-lnk" href="#">Todos los comentarios</a>
            </div>
        </div>

        <div v-if="cargandoPublicaciones" class="publ-message">Cargando Más Publicaciones...</div>
        <div v-else class="publ-message">No hay más publicaciones</div>
    </div>
    <div v-else class="no-publ-message">No tienes publicaciones creadas</div>
</template>

<script setup>
    import { ref, onMounted } from 'vue';
    import axios from 'axios';
    import router from '@/router';
    import { format } from 'date-fns';

    const token = ref('');

    const publicaciones = ref([]);
    const interaccionesPublicaciones = ref({});

    // Variables para la paginacion de publicaciones
    const page = ref(1);
    const size = ref(10);
    const total = ref(0);
    const hasNext = ref(false)
    const cargandoPublicaciones = ref(false)

    
    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('login');
            return;
        }
    }

    const formattedDate = (date) => {
        return format(new Date(date), "dd/MM/yyyy · h:mm aaa").toUpperCase();
    };


    async function getPosts(page = 1) {
        validarToken();

        try {
            const response = await axios.get('http://localhost:8000/perfil/me/publicaciones', {
                headers: {
                    Authorization: `Bearer ${token.value}`
                },
                params: {
                    page,
                    size: size.value,
                },
            });

            // Crea un nuevo array con las publicaciones previamente guardadas junto con las nuevas almacenadas en data
            const { data, total: totalPost, has_next } = response.data;

            console.log(response.data)

            // Crea un nuevo array con las publicaciones previamente guardadas junto con las nuevas almacenadas en data
            publicaciones.value = [...publicaciones.value, ...data]; 
            console.log(publicaciones.value)
            total.value = totalPost;
            hasNext.value = has_next;

            publicaciones.value.forEach(publicacion => {
                getNumInteracionesPublicacion(publicacion.id_publicacion);
            });
        } catch (error) {
            console.log(error);
            localStorage.removeItem('token');
            router.push('/login');
        }
    }


    async function getNumInteracionesPublicacion(idPublicacion) {
        interaccionesPublicaciones.value[idPublicacion] = { "cargando": true }
        try {
            const response = await axios.get(`http://localhost:8000/publicaciones/${idPublicacion}/interacciones`, {
                headers: {
                    Authorization: `Bearer ${token.value}`,
                },
            });
            interaccionesPublicaciones.value[idPublicacion] = {
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


    async function cargarPublicaciones() {
        if (!hasNext.value || cargandoPublicaciones.value) return;

        cargandoPublicaciones.value = true;
        page.value += 1;

        await getPosts(page.value);
        cargandoPublicaciones.value = false;
    }


    // Detecta cuando el usuario hace scroll en la parte iferior del contenedor
    function handleScroll() {
        const container = document.querySelector('.publicaciones-container');
        const inferiorContainer = container.scrollHeight - container.scrollTop <= container.clientHeight + 200;

        if (inferiorContainer && hasNext.value) {
            cargarPublicaciones();
        }
    }


    async function updatePost() {
        publicaciones.value = [];
        getPosts();
    }


    async function likeFunc(idPublicacion, publicacionLikeada) {
        const statusLikePrevio = publicacionLikeada;

        // Actualziacion visual de likes
        const interaccionActual = interaccionesPublicaciones.value[idPublicacion];
        interaccionActual.publicacionLikeada = !statusLikePrevio;
        interaccionActual.likes += statusLikePrevio ? -1 : 1;

        try {
            if (statusLikePrevio) {
                await axios.delete(`http://localhost:8000/publicaciones/${idPublicacion}/like`, {
                    headers: {
                        Authorization: `Bearer ${token.value}`,
                    }
                })
            } else {
                await axios.post(`http://localhost:8000/publicaciones/${idPublicacion}/like`, {}, {
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


    async function accederDetallesPublicacion(idPublicacion) {
        router.push(`/publicaciones/detalles/${idPublicacion}`);
    }


    defineExpose({
        updatePost,
    });


    onMounted(() => {
        page.value = 1;
        getPosts();
    })
</script>

<style scoped>
    .publicaciones-container {
        width: 100%;
        height: 100%;
        color: var(--color-text-primary);
        display: flex;
        flex-direction: column;
        align-items: center;
        overflow: auto;
        scrollbar-width: none;
        -ms-overflow-style: none;
        padding-bottom: 100px
    }

    .publicaciones-container::-webkit-scrollbar {
        display: none;
    }

    .publ-card {
        width: 75%;
        height: auto;
        padding: 15px 0;
        border-bottom: 1px solid var(--color-border);
    }

    .publ-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .info-usuario {
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .pfp-usuario {
        width: 40px;
        height: 40px;
        border-radius: 50%;
    }

    .nombre-usuario {
        font-size: 16px;
        font-weight: 600;
    }

    .publ-actions {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
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

    .publ-content {
        font-size: 14px;
        padding: 10px 0;
    }

    .publ-content:hover {
        cursor: pointer;
    }

    .publ-footer {
        display: flex;
        flex-direction: column;
        gap: 5px;
    }

    .publ-content-date {
        font-size: 12px;
        color: var(--color-text-secundary);
        margin-bottom: 5px;
    }

    .publ-btns {
        display: flex;
        gap: 9px;
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

    .publ-comentarios-lnk {
        font-size: 14px;
        color: var(--color-text-secundary);
    }

    .loading-message {
        font-size: 14px;
        color: var(--color-text-primary);
    }

    .publ-message {
        color: var(--color-text-secundary);
        font-size: 14px;
        padding-top: 15px;
    }

    .no-publ-message {
        width: 100%;
        height: 100px;
        color: var(--color-text-secundary);
        display: flex;
        align-items: center;
        justify-content: center;
    }
</style>