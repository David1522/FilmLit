<template>
    <div class="publ-actions">
        <el-dropdown v-if="props.isOwnComent" @command="handleCommand">
            <span class="el-dropdown-link">
                <fa icon="ellipsis" class="actions-btn"/>
            </span>
            <template #dropdown>
                <el-dropdown-menu>
                    <el-dropdown-item command="eliminar" class="eliminar-act">
                        <span class="publ-act"> Eliminar <fa icon="trash"/> </span>
                    </el-dropdown-item>
                </el-dropdown-menu>
            </template>
        </el-dropdown>
        <el-dropdown v-else @command="handleCommand">
            <span class="el-dropdown-link">
                <fa icon="ellipsis" class="actions-btn"/>
            </span>
            <template #dropdown>
                <el-dropdown-menu>
                    <el-dropdown-item command="reportar" class="eliminar-act">
                        <span class="publ-act"> Reportar <fa icon="circle-info"/> </span>
                    </el-dropdown-item>
                </el-dropdown-menu>
            </template>
        </el-dropdown>
    </div>
</template>

<script setup>
    import { defineProps, ref } from 'vue';
    import axios from 'axios';
    import router from '@/router';
    import Swal from 'sweetalert2';

    const emits = defineEmits();
    
    const token = ref('');
    const props = defineProps({
        isOwnComent: Boolean,
        idComentario: Number
    })

    // Añadir funcionalidad de repotar despues
    const handleCommand =  (command) => {
        if (command === 'eliminar') {
            eliminarComentario();
        } else {
            console.log('Publicacion reportada');
        }
    };

    async function eliminarComentario() {
        validarToken();

        try {
            const response = await axios.delete(`http://localhost:8000/publicaciones/comentario/${props.idComentario}`, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            });

            Swal.fire({
                title: '¡Comentario Eliminado!',
                icon: 'success',
                text: response.data.detail
            })
        } catch (error) {
            Swal.fire({
                title: 'Ha Ocurrido un Error',
                icon: 'error',
                text: error
            })
        }
    }

    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('/');
            return;
        }
    }
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