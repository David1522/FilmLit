<template>
    <div class="publ-actions">
        <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
                <fa icon="ellipsis" class="actions-btn"/>
            </span>
            <template #dropdown>
                <el-dropdown-menu v-if="publicacionPropia">
                    <el-dropdown-item command="editar" class="editar-act" type="warning">
                        <span class="publ-act"> Editar <fa icon="pencil"/> </span>
                    </el-dropdown-item>
                    
                    <el-dropdown-item command="eliminar" class="eliminar-act">
                        <span class="publ-act"> Eliminar <fa icon="trash"/> </span>
                    </el-dropdown-item>
                </el-dropdown-menu>

                <el-dropdown-menu v-else>
                    <el-dropdown-item command="reportar" class="eliminar-act">
                        <span class="publ-act"> Reportar <fa icon="circle-info"/> </span>
                    </el-dropdown-item>
                </el-dropdown-menu>
            </template>
        </el-dropdown>
    </div>
</template>

<script setup>
    import { defineProps, onMounted, ref } from 'vue';
    import { useRoute } from 'vue-router';
    import axios from 'axios';
    import Swal from 'sweetalert2';
    import router from '@/router';

    const emits = defineEmits();
    
    const route = useRoute();
    const token = ref('');

    const perfilId = ref(route.params.id);
    const props = defineProps({ idPublicacion: Number })
    const publicacionPropia = ref(false);

    // Funcionalidad de las opciones de la publicacion
    const handleCommand = (command) => {
        if (command === 'editar') {
            router.push(`/perfil/${perfilId.value}/publicacion-editar/${props.idPublicacion}`);
        } else {
            Swal.fire({
                title: "¿Estás Seguro de Borrar Esta Publicación?",
                icon: "info",
                text: "No podrás volver a recuperarla",
                showCancelButton: true,
                focusConfirm: false,
                confirmButtonText: 'Confirmar',
                cancelButtonText: 'Cancelar',
            }).then((result) => {
                if (result.isConfirmed) {
                    eliminarPublicacion();
                }
            });
        }
    };

    async function eliminarPublicacion() {
        validarToken();

        try {
            const response = await axios.delete(`http://localhost:8000/publicaciones/${props.idPublicacion}`, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            });

            console.log("Publicacion eliminada")

            Swal.fire({
                title: "Publicacion Eliminada",
                icon: 'success',
                text: response.data.Message
            })

            emits('posts-updated');
        } catch (error) {
            Swal.fire({
                title: "Error al Eliminar Publicacion",
                icon: 'error',
                text: 'Error al eliminar publicacion, intenta actualizar la pagina e intentarlo nuevamente.'
            })
        }
    }

    async function validacionPublicacionUsuario() {
        validarToken();

        try {
            const response = await axios.get(`http://localhost:8000/publicaciones/validacion/${props.idPublicacion}`, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            });
            publicacionPropia.value = response.data;
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

    onMounted(() => {
        validacionPublicacionUsuario();
    })
</script>

<style lang="scss" scoped>
    .publ-actions {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 10px;
        cursor: pointer;
    }

    .actions-btn {
        color: var(--color-text-primary);
        font-size: 15px;
    }

    .publ-act {
        display: flex;
        gap: 5px;
        align-items: center;
        justify-content: center;
    }

    .editar-act:hover > span { 
        color: green; 
    }

    .eliminar-act:hover > span { 
        color: red; 
    }
</style>
