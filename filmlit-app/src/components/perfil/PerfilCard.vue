<template>
    <div class="perfil-card" v-if="perfil">
        <img :src="fotoPerfilUrl" :key="fotoPerfilUrl" alt="pfp" class="usuario-pfp">

        <div class="detalles-perfil">
            <div class="ajustes-perfil">
                <p class="nombre-usuario">{{ perfil.usuario.nombre_usuario }}</p>
                <button v-if="perfilPropio" @click="router.push(`/perfil/${perfilId}/editar`)" class="btn-perfil">Editar</button>
                <button v-else class="btn-acc-options">
                    <follow-btn :id-perfil-creador="perfil.id_perfil" @update-follow-data="fetchAccountFollowData" />
                    <fa icon="ellipsis" />
                </button>
            </div>
            <div class="stats-perfil">
                <p class="stat">
                    <span class="num-seguidos" v-if="followers">{{ followers }}</span>
                    <span class="num-seguidores" v-else>0</span>
                    SEGUIDORES
                </p>
                <p class="stat">
                    <span class="num-seguidores" v-if="follows">{{ follows }}</span>
                    <span class="num-seguidores" v-else>0</span>
                    SIGUIENDO
                </p>
            </div>

            <div class="info-perfil">
                <p class="info info-nombre"><span>{{ perfil.nombre }}</span></p>
                <p class="info info-birthdate">
                    <span v-if="perfil.fecha_nacimiento">🎂 {{ perfil.fecha_nacimiento }}</span>
                </p>
                <p class="info info-desc">{{ perfil.descripcion }}</p>
            </div>
        </div>
    </div>
    <div v-else class="cargando">
        Cargando informacion del perfil...
    </div>
</template>

<script setup>
    import FollowBtn from '../publicaciones/FollowBtn.vue';

    import { ref, onMounted, computed } from 'vue';
    import { useRoute } from 'vue-router';
    import axios from 'axios';
    import router from '@/router';

    const route = useRoute();
    const token = ref('');

    const perfilId = ref(route.params.id);
    const perfil = ref(null)
    const followers = ref(0);
    const follows = ref(0);
    const perfilPropio = ref(false)

    async function fetchPerfilUsuario() {
        validarToken();

        try {
            const response = await axios.get(`http://localhost:8000/perfil/${perfilId.value}`, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            });
            perfil.value = response.data;
            fetchAccountFollowData();
        } catch (error) {
            console.log(error);
            localStorage.removeItem('token');
            router.push('/');
        }
    }

    async function fetchAccountFollowData() {
        try {
            const response = await axios.get(`http://localhost:8000/perfil/${perfilId.value}/follow/data`, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            });
            console.log(response.data);
            followers.value = response.data.followers;
            follows.value = response.data.follow;
        } catch (error) {
            console.log(error);
        }
    } 

    const fotoPerfilUrl = computed(() => {
        return perfil.value && perfil.value.foto_perfil
            ? `http://localhost:8000/static/fotos_perfil/${perfil.value.foto_perfil}?${Date.now}`
            : "http://localhost:8000/static/fotos_perfil/pfp-icon.jpg";
    });

    async function validarPerfilPropio() {
        validarToken();

        try {
            const response = await axios.get("http://localhost:8000/perfil/me/id", {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            });
            perfilPropio.value = perfilId.value == response.data;
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

    // Expone la funcion para que el componente padre la pueda llamar
    defineExpose({
        fetchPerfilUsuario,
    });

    onMounted(() => {
        fetchPerfilUsuario();
        validarPerfilPropio();

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
        object-fit: cover;
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
        margin-left: auto
    }

    .nombre-usuario {
        font-size: 15px;
        font-weight: 600;

    }

    .btn-perfil {
        color: var(--color-text-primary);
        background-color: var(--background-color-secondary);
        font-size: 15px;
        padding: 0 15px;
        border: none;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        margin-left: 10px
    }

    .btn-perfil:hover {
        opacity: 0.8;
    }

    .btn-acc-options {
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
        gap: 10px;
        margin-left: auto;
        margin-right: 100px
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

    .info-desc {
        word-break: break-all;
    }

    .cargando {
        text-align: center;
        padding: 20px;
    }
</style>