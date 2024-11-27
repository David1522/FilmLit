<template>
    <div class="publ-actions" v-if="!isPublUsurio">
        <el-dropdown @command="handleCommand">
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
    import { defineProps, onMounted, ref } from 'vue';
    import axios from 'axios';
    import router from '@/router';

    const emits = defineEmits();
    
    const token = ref('');
    const props = defineProps({
        idPublicacion: Number
    })

    const isPublUsurio = ref(null)

    async function validatePublUsuario() {
        validarToken();

        try {
            const response = await axios.get(`http://localhost:8000/publicaciones/validacion/${props.idPublicacion}`, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            })

            isPublUsurio.value = response.data;
        } catch (error) {
            console.log(error);
        }
    }

    // Añadir funcionalidad de repotar despues
    const handleCommand = (command) => {
        console.log("Holaa")
    };

    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('/');
            return;
        }
    }

    onMounted(() => {
        validatePublUsuario();
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
