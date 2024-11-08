<template>
    <div class="perfil-card" v-if="perfil">
        <img :src="fotoPerfilUrl" alt="pfp" class="usuario-pfp">

        <div class="detalles-perfil">
            <div class="ajustes-perfil">
                <p class="nombre-usuario">{{ perfil.usuario.nombre_usuario }}</p>
                <router-link to="/perfil/editar" class="btn-perfil">Editar</router-link>
                <button class="btn-acc-options"> <fa icon="ellipsis" /> </button>
            </div>
            <div class="stats-perfil">
                <p class="stat"><span class="num-seguidos"> {{ perfil.seguidos }} </span> segidos</p>
                <p class="stat"><span class="num-seguidores"> {{ perfil.seguidores }} </span> siguiendo</p>
            </div>

            <div class="info-perfil">
                <p class="info info-nombre"> <span> {{ perfil.nombre }} </span> </p>
                <p class="info info-birthdate"> <span v-if="perfil.fecha_nacimiento"> 🎂 {{ perfil.fecha_nacimiento }}</span> </p>
                <p class="info info-desc"> {{ perfil.descripcion }} </p>
            </div>
        </div>
    </div>
    <div v-else class="cargando">
        Cargando informacion del perfil...
    </div>
</template>

<script setup>
    import { ref, onMounted, computed } from 'vue';
    import axios from 'axios';
    import router from '@/router';
    
    const perfil = ref(null)

    async function fetchPerfilUsuario() {
        const token = localStorage.getItem('token');
        if (!token) {
            router.push('/login');
            return;
        }

        try {
            const response = await axios.get('http://localhost:8000/perfil/me', {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
            perfil.value = response.data
        } catch (error) {
            localStorage.removeItem('token');
            router.push('/login');
        }
    }


    const fotoPerfilUrl = computed(() => {
        console.log("Computing the profile picture URL");
        return perfil.value && perfil.value.foto_perfil
            ? `http://localhost:8000/static/fotos_perfil/${perfil.value.id_perfil}.jpg`
            : "http://localhost:8000/static/fotos_perfil/pfp-icon.jpg";
    });


    // Expone la funcion para que el componente padre la pueda llamar
    defineExpose({
        fetchPerfilUsuario,
    });


    onMounted(() => {
        fetchPerfilUsuario();
    })
</script>

<style scoped>
    .perfil-card {
        height: auto;
        display: grid;
        grid-template: 1fr / 1fr 2fr;
        border-bottom: 1px solid var(--color-border);
        padding-bottom: 15px;
    }

    .usuario-pfp {
        width: 125px;
        height: 125px;
        border-radius: 50%;
        justify-self: center;
    }

    .detalles-perfil {
        display: grid;
        grid-template: 25px 35px 1fr / 1fr;
        padding-left: 15px;
    }

    .ajustes-perfil {
        width: 100%;
        height: 100%;
        display: flex;
        gap: 10px;
    }

    .nombre-usuario {
        font-size: 15px;
        font-weight: 500;
    }

    .btn-perfil {
        color: var(--color-text-primary);
        background-color: var(--background-color-secondary);
        font-size: 15px;
        padding: 0 15px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .btn-perfil:hover {
        opacity: 0.8;
    }

    .btn-acc-options {
        width: 30px;
        height: 25px;
        color: var(--color-text-primary);
        background-color: var(--background-color-primary);
        font-size: 15px;
        padding: 0 15px;
        border: none;
        border-radius: 2px;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
    }

    .btn-acc-options:hover {
        transform: scale(1.1);
        transition: transform ease-in-out 300ms;
    }

    .stats-perfil {
        display: flex;
        align-items: center;
        gap: 15px
    }

    .stat {
        font-size: 14px;
    }

    .stat > span {
        font-size: 16;
        font-weight: 600;
    }

    .info {
        font-size: 14px;
    }

    .info-nombre {
        font-weight: 600;
    }

    .info-birthdate {
        font-size: 12px;
        padding-bottom: 5px;
    }

    .cargando {
        text-align: center;
        padding: 20px;
    }
</style>