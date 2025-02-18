<template>
    <div class="book-actions">
        <el-dropdown v-if="isBookUser" @command="handleCommand">

            <span class="el-dropdown-link">
                <fa icon="ellipsis" class="actions-btn"/>
            </span>

            <template #dropdown>
                <el-dropdown-menu>
                    <el-dropdown-item command="editar" class="edit-act">
                        <span class="publ-act"> Editar <fa icon="pencil"/> </span>
                    </el-dropdown-item>

                    <el-dropdown-item command="eliminar" class="delete-act">
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
                    <el-dropdown-item command="guardar" class="save-act">
                        <span class="publ-act"> Guardar <fa icon="heart"/> </span>
                    </el-dropdown-item>

                    <el-dropdown-item command="reportar" class="report-act">
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
        idBook: Number
    })

    const isBookUser = ref(null);

    async function validateBookUser() {
        validarToken();

        try {
            const response = await axios.get(`http://localhost:8000/libros/validacion/${props.idBook}`, {
                headers: {
                    Authorization: `Bearer ${token.value}`
                }
            });

            console.log(response.data)

            isBookUser.value = response.data;
        } catch (error) {
            console.log(error);
        }
    }

    const handleCommand =  (command) => {
        if (command == 'editar') {
            router.push({ name: 'EditarLibro', params: { idBook: props.idBook } });
        } else if (command === 'eliminar') {
            console.log("Libro eliminado.")
        } else if (command === 'guardar') {
            console.log('Libro guardado a favoritos.')
        } else {
            console.log('Publicacion reportada');
        }
    };

    async function validarToken() {
        token.value = localStorage.getItem('token');
        if (!token.value) {
            router.push('/');
            return;
        }
    }

    onMounted(() => {
        validateBookUser();
    })
</script>

<style lang="scss" scoped>
    .book-actions {
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

    .edit-act:hover > span { 
        color: orange; 
    }

    .delete-act:hover > span {
        color: red;
    }

    .save-act:hover > span { 
        color: red; 
    }

    .report-act:hover > span {
        color: blue;
    }
</style>