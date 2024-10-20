<template>
    <div class="modal-detalles-publicacion">
        <div class="publicacion-contenedor">
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
                    <p class="nombre-usuario">Juan</p>
                </div>
                
                <button class="follow-btn">Follow</button>
            </div>

            <div class="publ-contenido">
                <p class="publ-descripcion">Lorem ipsum dolor sit amet consectetur adipisicing elit. Aut optio quis, explicabo tempora repellendus, minima quam numquam fugit minus molestias incidunt tempore porro nemo unde omnis reprehenderit beatae nam iste.</p>
                <img src="../icons/img-post-example.jpg" alt="publ-img" class="publ-img">
            </div>

            <div class="publ-footer">
                <p class="publ-fecha">19/10/2024, 02:27 p. m.</p>

                <div class="publ-stats">
                    <button 
                        class="publ-f-btn">
                        <span> 0 </span>
                        <fa icon="heart"/>
                    </button>

                    <button class="publ-f-btn">
                        <span> 0 </span>
                        <fa icon="comment"/>
                    </button>
                </div>
            </div>

            <div class="comentario-form">
                <p>Holaa</p>
            </div>
        </div>
    </div>
</template>

<script setup>
    import router from '@/router';
import { ref } from 'vue';
    import { useRoute } from 'vue-router';

    const route = useRoute();
    const idPublicacion = ref(route.params.id)
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
        justify-content: center;
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
        margin-bottom: 10px
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
        align-items: center;
        justify-content: center;
        gap: 10px;
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
        height: 150px;
        border-top: 1px solid var(--color-border);
        border-bottom: 1px solid var(--color-border);
        padding: 50px;
    }
</style>