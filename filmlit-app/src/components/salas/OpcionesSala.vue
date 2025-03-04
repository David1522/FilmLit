<template>
    <div class="publ-actions">
        <el-dropdown v-if="props.isOwnSala" @command="handleCommand">
            <span class="el-dropdown-link">
                <fa icon="ellipsis" class="actions-btn"/>
            </span>
            <template #dropdown>
                <el-dropdown-menu>
                    <el-dropdown-item command="editar" class="editar-act">
                        <span class="publ-act"> Editar <fa icon="pen"/> </span>
                    </el-dropdown-item>
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
        isOwnSala: Boolean,
        idSala: Number
    })

    // Añadir funcionalidad de repotar despues
    const handleCommand =  (command) => {
        if (command === 'editar') {
            emits('editar-sala')
        } else if (command === 'eliminar') {
            emits('eliminar-sala')
        } else {
            emits('sala-reportada')
        }
    };

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